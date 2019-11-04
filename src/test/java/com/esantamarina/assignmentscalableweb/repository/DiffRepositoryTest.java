package com.esantamarina.assignmentscalableweb.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DiffRepositoryTest {

    @Autowired
    private DiffValueRepository repository;

    /**
        Scenario: Save entity using repository class
        Expected: Retrieve persisted entity
   */
    @Test
    public void saves_and_retrieves_entity() {
        DiffValueEntity entity = new DiffValueEntity("1");
        entity.setLeft(new byte[]{0, 1, 1});
        entity.setRight(new byte[]{0, 1, 0});

        repository.save(entity);

        DiffValueEntity result = repository.findById("1").orElse(null);
        assertThat(result, is(entity));
    }
}
