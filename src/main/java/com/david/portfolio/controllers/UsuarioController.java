package com.david.portfolio.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.david.portfolio.models.Papel;
import com.david.portfolio.models.Usuario;
import com.david.portfolio.repositories.PapelRepository;
import com.david.portfolio.repositories.UsuarioRepository;

@Controller
@RequestMapping("/user")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PapelRepository papelRepository;

	@Autowired
	private BCryptPasswordEncoder criptografia;

	@GetMapping("/inicio")
	public String inicio(@CurrentSecurityContext(expression = "authentication.name") String username) {
		Usuario usuario = usuarioRepository.findByUsername(username);

		String redirectURL = "";
		if (temAutorizacao(usuario, "ADMIN")) {
			redirectURL = "auth/admin/admin-inicio";
		} else if (temAutorizacao(usuario, "USER")) {
			redirectURL = "auth/user/user-inicio";
		}
		return redirectURL;
	}

	private boolean temAutorizacao(Usuario usuario, String papel) {
		for (Papel pp : usuario.getPapeis()) {
			if (pp.getPapel().equals(papel)) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/novo")
	public String adicionarUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "cadastrar-usuario";
	}

	@PostMapping("/salvar")
	public String salvarUsuario(@Valid Usuario usuario, BindingResult result,
			Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "cadastrar-usuario";
		}

		Usuario usr = usuarioRepository.findByUsername(usuario.getUsername());
		if (usr != null) {
			model.addAttribute("loginExiste", "Login já existe cadastrado");
			return "cadastrar-usuario";
		}

		Papel papel = papelRepository.findByPapel("USER");
		List<Papel> papeis = new ArrayList<Papel>();
		papeis.add(papel);
		usuario.setPapeis(papeis);

		String senhaCriptografia = criptografia.encode(usuario.getPassword());
		usuario.setPassword(senhaCriptografia);

		usuarioRepository.save(usuario);
		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
		return "redirect:/";
	}

	@RequestMapping("/admin/listar")
	public String listarUsuario(Model model) {
		model.addAttribute("usuarios", usuarioRepository.findAll());
		return "auth/admin/admin-listar-usuarios";
	}

	@GetMapping("/admin/apagar/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id inválido:" + id));
		usuarioRepository.delete(usuario);
		return "redirect:/user/admin/listar";
	}

	@GetMapping("/editar/{id}")
	public String editarUsuario(@PathVariable("id") long id, Model model) {
		Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
		if (!usuarioExistente.isPresent()) {
			throw new IllegalArgumentException("Usuário inválido:" + id);
		}
		Usuario usuario = usuarioExistente.get();
		model.addAttribute("usuario", usuario);
		return "auth/user/user-editar-usuario";
	}

	@PostMapping("/editar/{id}")
	public String editarUsuario(@PathVariable("id") long id,
			@Valid Usuario usuario, BindingResult result) {
		if (result.hasErrors()) {
			usuario.setId(id);
			return "auth/user/user-editar-usuario";
		}
		usuarioRepository.save(usuario);
		return "redirect:/user/admin/listar";
	}

	@GetMapping("/editarPapel/{id}")
	public String selecionarPapel(@PathVariable("id") long id, Model model) {
		Optional<Usuario> usuarioVelho = usuarioRepository.findById(id);
		if (!usuarioVelho.isPresent()) {
			throw new IllegalArgumentException("Usuário inválido:" + id);
		}
		Usuario usuario = usuarioVelho.get();
		model.addAttribute("usuario", usuario);

		model.addAttribute("listaPapeis", papelRepository.findAll());

		return "auth/admin/admin-editar-papel-usuario";
	}

	@PostMapping("/editarPapel/{id}")
	public String atribuirPapel(@PathVariable("id") long idUsuario,
			@RequestParam(value = "pps", required = false) int[] pps,
			Usuario usuario,
			RedirectAttributes attributes) {
		if (pps == null) {
			usuario.setId(idUsuario);
			attributes.addFlashAttribute("mensagem", "Pelo menos um papel deve ser informado");
			return "redirect:/user/editarPapel/" + idUsuario;
		} else {

			List<Papel> papeis = new ArrayList<Papel>();
			for (int i = 0; i < pps.length; i++) {
				long idPapel = pps[i];
				Optional<Papel> papelOptional = papelRepository.findById(idPapel);
				if (papelOptional.isPresent()) {
					Papel papel = papelOptional.get();
					papeis.add(papel);
				}
			}
			Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
			if (usuarioOptional.isPresent()) {
				Usuario usr = usuarioOptional.get();
				usr.setPapeis(papeis);
				usr.setAtivo(usuario.isAtivo());
				usuarioRepository.save(usr);
			}
		}
		return "redirect:/user/admin/listar";
	}

}
