package com.david.portfolio.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.david.portfolio.models.Jogo;
import com.david.portfolio.repositories.JogoRepository;

@Controller
@RequestMapping("/jogo")
public class JogoController {
	
	@Autowired
	private JogoRepository jogoRepository;
	
	@GetMapping("/novo")
	public String adicionarJogo(Model model) {
		model.addAttribute("jogo", new Jogo());
		return "auth/admin/admin-cadastrar-jogo";
	}
	
	@PostMapping("/salvar")
	public String salvarJogo(@Valid Jogo jogo, BindingResult result, 
				RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "auth/admin/admin-cadastrar-jogo";
		}	
		jogoRepository.save(jogo);
		attributes.addFlashAttribute("mensagem", "Jogo salvo com sucesso!");
		return "redirect:/jogo/admin/listar";
	}

	@RequestMapping("/admin/listar")
	public String listarJogo(Model model) {
		model.addAttribute("jogos", jogoRepository.findAll());		
		return "auth/admin/admin-listar-jogos";
	}

	@GetMapping("/admin/apagar/{id}")
	public String deletarJogo(@PathVariable("id") long id, Model model) {
		Jogo jogo = jogoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id inválido:" + id));
		jogoRepository.delete(jogo);
	    return "redirect:/jogo/admin/listar";
	}

	@GetMapping("/editar/{id}")
	public String editarJogo(@PathVariable("id") long id, Model model) {
		Optional<Jogo> jogoExistente = jogoRepository.findById(id);
		if (!jogoExistente.isPresent()) {
            throw new IllegalArgumentException("Jogo inválido:" + id);
        } 
		Jogo jogo = jogoExistente.get();
	    model.addAttribute("jogo", jogo);
	    return "auth/user/user-alterar-jogo";
	}
	
	@PostMapping("/editar/{id}")
	public String editarJogo(@PathVariable("id") long id, 
			@Valid Jogo jogo, BindingResult result) {
		if (result.hasErrors()) {
	    	jogo.setId(id);
	        return "auth/user/user-alterar-jogo";
	    }
	    jogoRepository.save(jogo);
	    return "redirect:/jogo/admin/listar";
	}

}