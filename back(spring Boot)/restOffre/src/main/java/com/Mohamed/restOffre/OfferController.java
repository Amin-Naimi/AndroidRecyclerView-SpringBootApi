package com.Mohamed.restOffre;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offre")
@AllArgsConstructor
public class OfferController {
    private final OffreRepository offreRepository;

    @GetMapping
    public List<Offres> getAll(){
        return offreRepository.findAll();
    }

    @GetMapping("/{uid}")
    public Offres getById(@PathVariable("uid") Long uid){
        return offreRepository.findById(uid).get();
    }

    @PostMapping
    public Offres saveOffre(@RequestBody Offres newOffre){
        return offreRepository.save(newOffre);
    }

    @DeleteMapping("{id}")
    public Boolean deleteOffre(@PathVariable("id") Long id){
        offreRepository.deleteById(id);
        return true;
    }

    @PutMapping("{id}")
    public Offres updateOffre(@PathVariable("id") Long id, @RequestBody Offres offres){
        offres.setId(id);
        return offreRepository.save(offres);
    }

}
