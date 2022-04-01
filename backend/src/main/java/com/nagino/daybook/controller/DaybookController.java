package com.nagino.daybook.controller;

import com.nagino.daybook.model.Daybook;
import com.nagino.daybook.model.Daybook;
import com.nagino.daybook.model.DaybookCategory;
import com.nagino.daybook.repository.DaybookCategoryRepository;
import com.nagino.daybook.repository.DaybookRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DaybookController extends BaseController {
    @Autowired
    DaybookCategoryRepository daybookCategoryRepository;
    @Autowired
    DaybookRepository daybookRepository;

    @GetMapping("/daybooks")
    public ResponseEntity<List<Daybook>> getAllDaybooks(@RequestParam(required = false) Integer year) {
        Specification<Daybook> specification = (root, query, cb) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            // equal ownerId
            if (getCurrentUserId() != null){
                Predicate namePredicate = cb.equal(root.get("ownerId"), getCurrentUserId());
                predicatesList.add(namePredicate);
            }
            if (year != null) {
                Predicate namePredicate = cb.equal(cb.function("YEAR", Integer.class, root.get("recordDate")), year);
                predicatesList.add(namePredicate);
            }
            Predicate[] predicates = new Predicate[predicatesList.size()];
            query.orderBy(cb.desc(root.get("recordDate")));
            return cb.and(predicatesList.toArray(predicates));
        };
        return new ResponseEntity<>(daybookRepository.findAll(specification), HttpStatus.OK);
    }

    @GetMapping("/daybookYearList")
    public ResponseEntity<List<Integer>> getDaybookYearList() {
        return new ResponseEntity<>(daybookRepository.getYearList(getCurrentUserId()), HttpStatus.OK);
    }


    @GetMapping("/daybooks/{id}")
    public ResponseEntity<Daybook> getDaybookById(@PathVariable("id") long id) {
        Optional<Daybook> daybook = daybookRepository.findByIdAndOwnerId(id, getCurrentUserId());

        return daybook.map(daybookCategory -> new ResponseEntity<>(daybookCategory, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/daybookCategories/{daybookCategoryId}/daybooks")
    public ResponseEntity<Daybook> createDaybook(@PathVariable(value = "daybookCategoryId") Long daybookCategoryId,
                                                 @RequestBody Daybook daybookRequest) {
        try {
            Daybook daybook = daybookCategoryRepository.findByIdAndOwnerId(daybookCategoryId, getCurrentUserId()).map(daybookCategory -> {
                daybookRequest.setDaybookCategory(daybookCategory);
                return daybookRepository.save(daybookRequest);
            }).orElseThrow(() -> new Exception("Not found Daybook with id = " + daybookCategoryId));

            return new ResponseEntity<>(daybook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/daybooks/{id}")
    public ResponseEntity<Daybook> updateDaybook(@PathVariable("id") long id, @RequestBody Daybook daybookRequest) {

        try {
            Daybook daybook = daybookRepository.findByIdAndOwnerId(id, getCurrentUserId())
                    .orElseThrow(() -> new Exception("Not found Daybook with id = " + id));
            daybook.setDaybookCategory(daybookRequest.getDaybookCategory());
            daybook.setTitle(daybookRequest.getTitle());
            daybook.setAmount(daybookRequest.getAmount());
            daybook.setRecordDate(daybookRequest.getRecordDate());
            daybook.setComment(daybookRequest.getComment());

            return new ResponseEntity<>(daybookRepository.save(daybookRequest), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/daybooks/{id}")
    public ResponseEntity<HttpStatus> deleteDaybook(@PathVariable("id") long id) {
        try {
            Optional<Daybook> daybook = daybookRepository.findByIdAndOwnerId(id, getCurrentUserId());
            if (daybook.isPresent()) {
                daybookRepository.deleteByIdAndOwnerId(id, getCurrentUserId());
            } else {
                throw new Exception("Not found Daybook with id = " + id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
