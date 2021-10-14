package com.nagino.daybook.controller;

import com.nagino.daybook.model.DaybookCategory;
import com.nagino.daybook.repository.DaybookCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DaybookCategoryController extends BaseController {
    @Autowired
    DaybookCategoryRepository daybookCategoryRepository;

    @GetMapping("/daybookCategories")
    public ResponseEntity<List<DaybookCategory>> getAllDaybookCategories(@RequestParam(required = false) String category, String name) {
        Specification<DaybookCategory> specification = new Specification<DaybookCategory>(){
            @Override
            public Predicate toPredicate(Root<DaybookCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
                return cb.and(predicatesList.toArray(predicates));
            }
        };
        return new ResponseEntity<>(daybookCategoryRepository.findAll(specification), HttpStatus.OK);
    }

    @GetMapping("/daybookCategories/{id}")
    public ResponseEntity<DaybookCategory> getDaybookCategoryById(@PathVariable("id") long id) {
        Optional<DaybookCategory> DaybookCategoryData = daybookCategoryRepository.findByIdAndOwnerId(id, getCurrentUserId());

        if (DaybookCategoryData.isPresent()) {
            return new ResponseEntity<>(DaybookCategoryData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        Optional<DaybookCategory> daybookCategoryData = daybookCategoryRepository.findById(id);

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
    public ResponseEntity<HttpStatus> deleteDaybookCategory(@PathVariable("id") long id) {
        try {
            daybookCategoryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
