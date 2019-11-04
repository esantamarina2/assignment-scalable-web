package com.esantamarina.assignmentscalableweb.service;

import com.esantamarina.assignmentscalableweb.controller.ApiException;
import com.esantamarina.assignmentscalableweb.domain.BinaryData;
import com.esantamarina.assignmentscalableweb.domain.Diff;
import com.esantamarina.assignmentscalableweb.domain.DiffUtilities;
import com.esantamarina.assignmentscalableweb.repository.DiffValueEntity;
import com.esantamarina.assignmentscalableweb.repository.DiffValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Service
public class DiffService {

    private DiffValueRepository repository;

    @Autowired
    public DiffService(DiffValueRepository repository) {
        this.repository = repository;
    }

    public void putLeftValue(String id, BinaryData data) {
        DiffValueEntity entity = findDiffValueEntity(id);
        entity.setLeft(data.getData());
        repository.save(entity);
    }

    public void putRightValue(String id, BinaryData data) {
        DiffValueEntity entity = findDiffValueEntity(id);
        entity.setRight(data.getData());
        repository.save(entity);
    }

    private DiffValueEntity findDiffValueEntity(String id){
        return repository.findById(id).orElse(new DiffValueEntity(id));
    }


    public Diff calculateDiff(String id) throws ApiException {
        DiffValueEntity entity = validateAndGet(id);
        String diff = computeDiff(entity.getLeft(), entity.getRight());
        return new Diff(entity.getId(), entity.getLeft(), entity.getRight(), diff);
    }

    private DiffValueEntity validateAndGet(String id) throws ApiException {
        DiffValueEntity entity = repository.findById(id)
                .orElseThrow(() -> new ApiException(PRECONDITION_FAILED, DiffUtilities.LEFT_SIDE + " and " + DiffUtilities.RIGHT_SIDE + " are missing for computing diff {" + id + "}"));

        checkSide(entity.getLeft(), DiffUtilities.LEFT_SIDE, id);
        checkSide(entity.getRight(), DiffUtilities.RIGHT_SIDE, id);

        return entity;
    }

    private boolean checkSide(byte[] side, String sideLabel, String id) throws ApiException {
        if (side == null) {
            throw new ApiException(PRECONDITION_FAILED, sideLabel + " part is missing for computing diff {" + id + "}");
        }
        return false;
    }

    private String computeDiff(byte[] left, byte[] right) {
        int diffs = DiffUtilities.countDifferentBytes(left, right);

        if (diffs == 0) {
            return "Left and Right are the same";
        }

        if (left.length > right.length) {
            return String.format("Left is longer than Right, and %s byte(s) are different", diffs);
        }

        if (left.length < right.length) {
            return String.format("Right is longer than Left, and %s bytes(s) are different", diffs);
        }

        return String.format("Left and Right have the same length, but %s byte(s) are different", diffs);
    }
}
