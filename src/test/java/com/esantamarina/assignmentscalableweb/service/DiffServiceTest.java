package com.esantamarina.assignmentscalableweb.service;

import com.esantamarina.assignmentscalableweb.controller.ApiException;
import com.esantamarina.assignmentscalableweb.domain.BinaryData;
import com.esantamarina.assignmentscalableweb.domain.Diff;
import com.esantamarina.assignmentscalableweb.repository.DiffValueEntity;
import com.esantamarina.assignmentscalableweb.repository.DiffValueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@RunWith(JUnit4.class)
public class DiffServiceTest {

    private DiffValueRepository mockRepository;
    private DiffService diffService;

    @Before
    public void setup() {
        mockRepository = mock(DiffValueRepository.class);
        diffService = new DiffService(mockRepository);
    }

    /**
     Scenario: Process left part value
     Expected: Entity value is equal to domain value
    */
    @Test
    public void puts_left_value() {
        String id = "1";
        BinaryData data = mockContent();

        diffService.putLeftValue(id, data);

        ArgumentCaptor<DiffValueEntity> captor = ArgumentCaptor.forClass(DiffValueEntity.class);
        verify(mockRepository).save(captor.capture());

        DiffValueEntity entity = captor.getValue();
        assertThat(entity.getId(), is(id));
        assertThat(entity.getLeft(), is(data.getData()));
    }

    /**
     Scenario: Process right part value
     Expected: Entity value is equal to domain value
    */
    @Test
    public void puts_right_value() {
        String id = "1";
        BinaryData data = mockContent();

        diffService.putRightValue(id, data);

        ArgumentCaptor<DiffValueEntity> captor = ArgumentCaptor.forClass(DiffValueEntity.class);
        verify(mockRepository).save(captor.capture());

        DiffValueEntity entity = captor.getValue();
        assertThat(entity.getId(), is(id));
        assertThat(entity.getRight(), is(data.getData()));
    }

    private BinaryData mockContent(){
        byte[] content = {1, 2};
        return new BinaryData(content);
    }

    /**
     Scenario: Process the same right and left values
     Expected: Both parts are equals
    */
    @Test
    public void compute_diff_left_and_right_equals() throws ApiException {
        String id = "1";
        DiffValueEntity entity = new DiffValueEntity(id);

        entity.setLeft(new byte[]{0, 1});
        entity.setRight(new byte[]{0, 1});

        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        Diff result = diffService.calculateDiff(id);

        verify(mockRepository).findById(id);

        assertThat(result.getId(), is(id));
        assertThat(result.getDiff(), is("Left and Right are the same"));
    }

    /**
     Scenario: Process different right and left values
     Expected: Length and content are different
   */
    @Test
    public void compute_diff_left_and_right_different() throws ApiException {
        String id = "1";
        DiffValueEntity entity = new DiffValueEntity(id);
        entity.setLeft(new byte[]{0, 1, 0});
        entity.setRight(new byte[]{0, 1});

        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        Diff result = diffService.calculateDiff(id);

        verify(mockRepository).findById(id);

        assertThat(result.getId(), is(id));
        assertThat(result.getDiff(), is("Left is longer than Right, and 1 byte(s) are different"));
    }

    /**
      Scenario: Calculate diff with empty part
      Expected: Exception - Precondition failed
    */
    @Test
    public void fail_compute_diff_where_left_part_is_missing() {
        String id = "1";
        DiffValueEntity entity = new DiffValueEntity(id);
        entity.setRight(new byte[]{0, 1});

        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        try {
            diffService.calculateDiff(id);
            assert false;
        } catch (ApiException e) {
            verify(mockRepository).findById(id);
            assertThat(e.getStatus(), is(PRECONDITION_FAILED));
            assertThat(e.getMessage(), is("Left part is missing for computing diff {1}"));
        }
    }
}
