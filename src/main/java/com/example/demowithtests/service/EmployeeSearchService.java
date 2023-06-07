package com.example.demowithtests.service;

import com.example.demowithtests.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmployeeSearchService implements EmployeeSearchServiceImpl {
    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;
}
