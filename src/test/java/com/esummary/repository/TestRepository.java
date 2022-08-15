package com.esummary.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.entity.TestModel;


@Repository
public interface TestRepository extends CrudRepository<TestModel, String>{
	
}
