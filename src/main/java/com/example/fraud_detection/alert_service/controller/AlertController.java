package com.example.fraud_detection.alert_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fraud_detection.alert_service.service.AlertService;
import com.example.fraud_detection.alert_service.entity.Alert;
i

@RestController 
@RequestMapping ("/Alerts")
public class AlertController {

    public AlertService alertService;

    public AlertController(AlertService alertService){
        this.alertService = alertService;
    }
    
    @GetMapping 
    public ResponseEntity<Alert> getAlerts(){

    }

    @GetMapping ("/Id")
    public ResponseEntity<Alert> getAlert(@PathVariable  Long Id){

    }

    public ResponseEntity<Alert> updateAlert(@PathVariable  Long Id,
                                            @RequestBody UpdateAlertStatusRequest request){

    }
}

