package com.Mohamed.restOffre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offres {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String intutile;
    private String specialiter;
    private String socite;
    private int nbpostes;
    private String pays;

}
