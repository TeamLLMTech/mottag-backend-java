package br.com.mottag.api.controller;

import br.com.mottag.api.dto.PatioRequestDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patios-page")
public class PatioPageController {
    private final PatioService patioService;

    public PatioPageController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping("")
    public String listPatios(Model model) {
        List<PatioResponseDTO> patios = patioService.findAllNoPage();
        model.addAttribute("patios", patios);
        return "patio";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patio", new PatioRequestDTO());
        model.addAttribute("form", true);
        model.addAttribute("formTitle", "Novo Pátio");
        model.addAttribute("formAction", "/patios-page/new");
        return "patio";
    }

    @PostMapping("/new")
    public String createPatio(@Valid PatioRequestDTO patio, Model model) {
        patioService.save(patio);
        return "redirect:/patios-page";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        PatioResponseDTO patio = patioService.findById(id);
        PatioRequestDTO patioRequest = new PatioRequestDTO();
        patioRequest.setNome(patio.getNome());
        patioRequest.setLayout(patio.getLayout());
        patioRequest.setEndereco(patio.getEndereco());
        model.addAttribute("patio", patioRequest);
        model.addAttribute("form", true);
        model.addAttribute("formTitle", "Editar Pátio");
        model.addAttribute("formAction", "/patios-page/edit/" + id);
        return "patio";
    }

    @PostMapping("/edit/{id}")
    public String editPatio(@PathVariable Long id, @Valid PatioRequestDTO patio) {
        patioService.update(id, patio);
        return "redirect:/patios-page";
    }

    @GetMapping("/delete/{id}")
    public String deletePatio(@PathVariable Long id) {
        patioService.delete(id);
        return "redirect:/patios-page";
    }
}