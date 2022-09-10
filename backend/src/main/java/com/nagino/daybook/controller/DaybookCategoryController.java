package com.nagino.daybook.controller;

import com.nagino.daybook.model.DaybookCategory;
import com.nagino.daybook.repository.DaybookCategoryRepository;
import com.nagino.daybook.repository.DaybookRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DaybookCategoryController extends BaseController {
    @Autowired
    DaybookCategoryRepository daybookCategoryRepository;
    @Autowired
    DaybookRepository daybookRepository;

    @GetMapping("/daybookCategories")
    public ResponseEntity<List<DaybookCategory>> getAllDaybookCategories(@RequestParam(required = false) String category, String name) {
        Specification<DaybookCategory> specification = (root, query, cb) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            // equal ownerId
            if (getCurrentUserId() != null){
                Predicate namePredicate = cb.equal(root.get("ownerId"), getCurrentUserId());
                predicatesList.add(namePredicate);
            }
            // equal category
            if (!StringUtils.isEmpty(category)){
                Predicate namePredicate = cb.equal(root.get("category"), category);
                predicatesList.add(namePredicate);
            }
            // like %name%
            if (!StringUtils.isEmpty(name)){
                Predicate nickNamePredicate = cb.like(root.get("name"), '%'+name+'%');
                predicatesList.add(nickNamePredicate);
            }
            Predicate[] predicates = new Predicate[predicatesList.size()];
            query.orderBy(cb.asc(root.get("category")), cb.asc(root.get("sortNum")));
            return cb.and(predicatesList.toArray(predicates));
        };
        return new ResponseEntity<>(daybookCategoryRepository.findAll(specification), HttpStatus.OK);
    }

    @GetMapping("/daybookCategories/{id}")
    public ResponseEntity<DaybookCategory> getDaybookCategoryById(@PathVariable("id") long id) {
        Optional<DaybookCategory> daybookCategoryData = daybookCategoryRepository.findByIdAndOwnerId(id, getCurrentUserId());

        return daybookCategoryData.map(daybookCategory -> new ResponseEntity<>(daybookCategory, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/daybookCategories")
    public ResponseEntity<DaybookCategory> createDaybookCategory(@RequestBody DaybookCategory daybookCategory) {
        try {
            DaybookCategory newDaybookCategory = new DaybookCategory(daybookCategory.getCategory(), daybookCategory.getName(), getCurrentUserId(), daybookCategory.getSortNum());
            DaybookCategory _daybookCategory = daybookCategoryRepository.save(newDaybookCategory);
            return new ResponseEntity<>(_daybookCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/daybookCategories/{id}")
    public ResponseEntity<DaybookCategory> updateDaybookCategory(@PathVariable("id") long id, @RequestBody DaybookCategory daybookCategory) {
        Optional<DaybookCategory> daybookCategoryData = daybookCategoryRepository.findByIdAndOwnerId(id, getCurrentUserId());

        if (daybookCategoryData.isPresent()) {
            DaybookCategory _daybookCategory = daybookCategoryData.get();
            _daybookCategory.setCategory(daybookCategory.getCategory());
            _daybookCategory.setName(daybookCategory.getName());
            _daybookCategory.setSortNum(daybookCategory.getSortNum());
            return new ResponseEntity<>(daybookCategoryRepository.save(_daybookCategory), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/daybookCategories/{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteDaybookCategory(@PathVariable("id") Long id) {
        try {
            Optional<DaybookCategory> daybookCategory = daybookCategoryRepository.findByIdAndOwnerId(id, getCurrentUserId());
            if (daybookCategory.isPresent()) {
                daybookRepository.deleteByDaybookCategoryAndOwnerId(daybookCategory.get(), getCurrentUserId());
                daybookCategoryRepository.deleteByIdAndOwnerId(id, getCurrentUserId());
            } else {
                throw new Exception("Not found DaybookCategory with id = " + id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
