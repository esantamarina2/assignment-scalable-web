package com.esantamarina.assignmentscalableweb.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DiffValueEntity {

    @Id
    private String id;
    private byte[] left;
    private byte[] right;

    public DiffValueEntity(String id) {
        this.id = id;
    }
}
