package br.com.mottag.api.controller;

import br.com.mottag.api.dto.MotoRequestDTO;
import br.com.mottag.api.dto.MotoResponseDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.model.StatusMoto;
import br.com.mottag.api.service.MotoService;
import br.com.mottag.api.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/motos-page")
public class MotoPageController {
    private final MotoService motoService;
    private final PatioService patioService;

    public MotoPageController(MotoService motoService, PatioService patioService) {
        this.motoService = motoService;
        this.patioService = patioService;
    }

    @GetMapping("")
    public String listMotos(Model model) {
        List<MotoResponseDTO> motos = motoService.findAllNoPage();
        model.addAttribute("motos", motos);
        return "moto";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("moto", new MotoRequestDTO());
        model.addAttribute("form", true);
        model.addAttribute("formTitle", "Nova Moto");
        model.addAttribute("formAction", "/motos-page/new");
        model.addAttribute("statusOptions", StatusMoto.values());
        model.addAttribute("patioOptions", patioService.findAllNoPage());
        return "moto";
    }

    @PostMapping("/new")
    public String createMoto(@Valid MotoRequestDTO moto, Model model) {
        motoService.save(moto);
        return "redirect:/motos-page";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MotoResponseDTO moto = motoService.findById(id);
        MotoRequestDTO motoRequest = new MotoRequestDTO();
        motoRequest.setModelo(moto.getModelo());
        motoRequest.setPlaca(moto.getPlaca());
        motoRequest.setStatus(moto.getStatus());
        motoRequest.setIdPatio(moto.getIdPatio());
        model.addAttribute("moto", motoRequest);
        model.addAttribute("form", true);
        model.addAttribute("formTitle", "Editar Moto");
        model.addAttribute("formAction", "/motos-page/edit/" + id);
        model.addAttribute("statusOptions", StatusMoto.values());
        model.addAttribute("patioOptions", patioService.findAllNoPage());
        return "moto";
    }

    @PostMapping("/edit/{id}")
    public String editMoto(@PathVariable Long id, @Valid MotoRequestDTO moto) {
        motoService.update(id, moto);
        return "redirect:/motos-page";
    }

    @GetMapping("/delete/{id}")
    public String deleteMoto(@PathVariable Long id) {
        motoService.delete(id);
        return "redirect:/motos-page";
    }
}
