package com.esantamarina.assignmentscalableweb.domain;

import lombok.Data;

@Data
public class Diff {

    private final String id;
    private final byte[] left;
    private final byte[] right;
    private final String diff;

}
