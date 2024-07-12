package com.example.zamieszkajtu.controller;

import com.example.zamieszkajtu.model.Property;
import com.example.zamieszkajtu.model.PropertyDto;
import com.example.zamieszkajtu.repository.PropertyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping({"","/"})
    public String stronaGlowna() {
        return "stronaglowna";
    }

    @GetMapping({"/properties"})
    public String showPropertyList(Model model) {
        List<Property> properties = propertyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("properties", properties);
        return "properties/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        PropertyDto propertyDto = new PropertyDto();
        model.addAttribute("propertyDto", propertyDto);
        return "properties/createProperty";
    }

    @PostMapping("/create")
    public String createProperty(@Valid @ModelAttribute PropertyDto propertyDto,
                                 BindingResult bindingResult) {
        if (propertyDto.getImageFile().isEmpty()){
            bindingResult.addError(new FieldError("propertyDto", "imageFile",
                    "Zdjęcie nieruchomości jest wymagane"));
        }
        if (bindingResult.hasErrors()) {
            return "properties/createProperty";
        }

        //zapis pliku
        MultipartFile imageFile = propertyDto.getImageFile();
        String fileName = imageFile.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + fileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        Property property = new Property();
        property.setTitleProperty(propertyDto.getTitleProperty());
        property.setCategoryProperty(propertyDto.getCategoryProperty());
        property.setCity(propertyDto.getCity());
        property.setAddress(propertyDto.getAddress());
        property.setDescription(propertyDto.getDescription());
        property.setPrice(propertyDto.getPrice());
        property.setImageName(fileName);

        propertyRepository.save(property);

        return "redirect:/properties";
    }

    @GetMapping("properties/edit")
    public String showEditForm(Model model, @RequestParam Long id) {

        try {
            Property property = propertyRepository.findById(id).get();
            model.addAttribute("property", property);

            PropertyDto propertyDto = new PropertyDto();
            propertyDto.setTitleProperty(property.getTitleProperty());
            propertyDto.setCategoryProperty(property.getCategoryProperty());
            propertyDto.setCity(property.getCity());
            propertyDto.setAddress(property.getAddress());
            propertyDto.setDescription(property.getDescription());
            propertyDto.setPrice(property.getPrice());
            model.addAttribute("propertyDto", propertyDto);
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return "redirect:/properties";
        }

        return "properties/editProperty";
    }

    @PostMapping("properties/edit")
    String updateProperty(@Valid @RequestParam Long id, Model model,
                          PropertyDto propertyDto,
                          BindingResult bindingResult) {

        try {
            Property property = propertyRepository.findById(id).get();
            model.addAttribute("property", property);

            if (bindingResult.hasErrors()) {
                return "properties/editProperty";
            }

            if (!propertyDto.getImageFile().isEmpty()) {
                String uploadDir = "public/images/";
                Path olduploadPath = Paths.get(uploadDir + property.getImageName());

                try {
                    Files.delete(olduploadPath);
                }
                catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }

                MultipartFile imageFile = propertyDto.getImageFile();
                String fileName = imageFile.getOriginalFilename();

                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + fileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                property.setImageName(fileName);
            }

            property.setTitleProperty(propertyDto.getTitleProperty());
            property.setCategoryProperty(propertyDto.getCategoryProperty());
            property.setCity(propertyDto.getCity());
            property.setAddress(propertyDto.getAddress());
            property.setDescription(propertyDto.getDescription());
            property.setPrice(propertyDto.getPrice());

            propertyRepository.save(property);
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return "redirect:/properties";
    }

    @GetMapping("/properties/delete")
    public String deleteProperty(@RequestParam Long id) {

        try {
            Property property = propertyRepository.findById(id).get();

            Path imagePath = Paths.get("public/images/" + property.getImageName());

            try {
                Files.delete(imagePath);
            }
            catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            propertyRepository.delete(property);

        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return "redirect:/properties";
    }
}
