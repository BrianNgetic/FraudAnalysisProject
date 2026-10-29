package com.example.fraud_detection.alert_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.example.fraud_detection.alert_service.service.AlertService;
import com.example.fraud_detection.alert_service.dto.AlertRequestDto;

import com.example.fraud_detection.alert_service.dto.UpdateAlertStatusRequest;




@RestController 
@RequestMapping ("/Alerts")
public class AlertController {

    public AlertService alertService;

    public AlertController(AlertService alertService){
        this.alertService = alertService;
    }
    
    @GetMapping 
    public ResponseEntity<Page<AlertRequestDto>> getAlerts(@RequestParam(defaultValue = "1") int page){

        return ResponseEntity.ok(alertService.getAlertsInPages(page));


    }

    //this one is for my dashboard, grabs the latest 10
    @GetMapping("/latest")
    public ResponseEntity<Page<AlertRequestDto>> getLatestAlerts(){
        return ResponseEntity.ok(alertService.getLatestAlerts());
    }

    
    @GetMapping ("/Id")
    public ResponseEntity<AlertRequestDto> getAlert(@PathVariable  Long Id) throws Exception{ 
            return ResponseEntity.ok(alertService.getSingleAlert(Id));
    }

    @PatchMapping("/Id")
    public ResponseEntity<?> updateAlert(@PathVariable  Long Id,
                                        @RequestBody UpdateAlertStatusRequest request) 
                                        throws Exception {

                alertService.patchAlertRequest(request, Id);
                 return ResponseEntity.ok().build();
    }
}

