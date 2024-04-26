package com.jason.elearning.controller;

import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.PlanType;
import com.jason.elearning.entity.constants.UserActive;
import com.jason.elearning.entity.request.PlanCourseRequest;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.plan.PlanService;
import com.jason.elearning.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RestController
@Transactional
@RequestMapping("/api/")
public class OrganizationController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private PlanService planService;
    @GetMapping("v1/list-organization-member")
    public ResponseEntity<?> listOrganizationMember(@RequestParam Long organizationId) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.listOrganizationMember(organizationId)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/list-organizations")
    public ResponseEntity<?> listOrganizations(@RequestParam(required = false) UserActive active,
                                               @RequestParam(required = false) String name) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.listOrganizations(active,name)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/verify-organization")
    public ResponseEntity<?> listOrganizations(@RequestParam Long id) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.verifyLecture(id)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/create-plan")
    public ResponseEntity<?> createPlan(@RequestParam PlanType type) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", planService.newPlaneContact(type)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/add-user-to-organization")
    public ResponseEntity<?> addUserToOrganization(@Valid @RequestBody final User request) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.addToOrganization(request)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/add-plan-course")
    public ResponseEntity<?> addUserToOrganization(@Valid @RequestBody final PlanCourseRequest request) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.addPlanCourse(request)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/get-plan")
    public ResponseEntity<?> getPlan() {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.getPlan()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/get-org-course")
    public ResponseEntity<?> getOrgCourse(@RequestParam Long id) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", planService.getOrgCourse(id)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
