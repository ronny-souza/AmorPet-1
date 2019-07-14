package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Animal;
import com.example.demo.model.Usuario;
import com.example.demo.service.AnimalService;
import com.example.demo.util.Utilidade;

@Controller
@RequestMapping("/adm")
public class AdmController {
	@Autowired
	private AnimalService animalService;

	@GetMapping({ "/perfil", "/perfil/editar" })
	public ModelAndView exibirPerfil(HttpSession session) {
		ModelAndView mv = new ModelAndView("/perfil");
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		mv.addObject("usuario", usuario);
		return mv;
	}

	@GetMapping("/candidatos")
	public ModelAndView exibirCandidatos() {
		return new ModelAndView("adm/candidatos");
	}

	@GetMapping("/cadastro/animal")
	public ModelAndView cadastroAnimal() {
		ModelAndView mv = new ModelAndView("/adm/cadanimal");
		mv.addObject("animal", new Animal());
		return mv;
	}

	@PostMapping("/cadastro/animal")
	public String cadastroAnimal(@Valid Animal animal, BindingResult br, RedirectAttributes ra) {
		if (br.hasErrors()) {
			ra.addFlashAttribute("error", "Não foi possível cadastrar o animal");
			return "redirect:/adm/cadastro/animal";
		}
		try {
			String path = Utilidade.caminhoParaImagem(animal.getFile().getOriginalFilename());
			File destino = new File(path);
			animal.getFile().transferTo(destino);
			animal.setCaminhoFoto("/img/" + animal.getFile().getOriginalFilename());
			animal.setDataRegistro(LocalDate.now());
			this.animalService.criarAnimal(animal);

			ra.addFlashAttribute("sucesso", "Animal cadastrado com sucesso!");

		} catch (IllegalStateException | IOException e) {
			ra.addFlashAttribute("error", "Não foi possível cadastrar o animal");
			System.out.println(e.getMessage());
		} catch (Exception e) {
			ra.addFlashAttribute("error", e.getMessage());
		}

		return "redirect:/adm/cadastro/animal";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/user/logout";
	}
}
