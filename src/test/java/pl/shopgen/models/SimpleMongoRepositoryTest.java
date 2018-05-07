package pl.shopgen.models;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class SimpleMongoRepositoryTest<T extends SimpleObject, R extends MongoRepository<T, String>> {

    protected T savedObject;

    protected List<T> savedObjects;

    @Autowired
    protected R repository;

    @Test
    public void saveAndFindObject() {
        saveObject();
        T foundObject = repository.findById(savedObject.getId()).orElse(null);
        assertEquals(savedObject, foundObject);
    }

    private void saveObject() {
        savedObject = repository.save(getObject());
    }

    public abstract T getObject();

    @Test
    public void deleteObject() {
        saveObject();
        Optional<T> foundedObject;
        repository.deleteById(savedObject.getId());
        foundedObject = repository.findById(savedObject.getId());
        assertFalse(foundedObject.isPresent());
    }

    @Test
    public void update() {
        saveObject();
        T updatedObject;
        T changedObject = getChangedObject(savedObject);

        updatedObject = repository.save(changedObject);

        assertEquals(changedObject, updatedObject);
    }

    public abstract T getChangedObject(T object);

    @Test
    public void findAll() {
        saveManyObjects();

        Iterable<T> foundedObjects;
        List<T> foundedObjectList = new ArrayList<>();

        foundedObjects = repository.findAllById(savedObjects.stream().map(T::getId).collect(Collectors.toList()));

        foundedObjects.forEach(foundedObjectList::add);
        assertEquals(savedObjects, foundedObjectList);
    }

    private void saveManyObjects() {
        savedObjects = repository.saveAll(getObjects());
    }

    public abstract List<T> getObjects();

    @Test
    public void deleteAll() {
        saveManyObjects();
        Iterable<T> foundedObjects;

        repository.deleteAll(savedObjects);
        foundedObjects = repository.findAllById(savedObjects.stream().map(T::getId).collect(Collectors.toList()));
        assertFalse(foundedObjects.iterator().hasNext());
    }

    @After
    public void clean() {
        repository.deleteAll();
    }
}
