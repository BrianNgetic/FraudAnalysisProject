package com.example.fraud_detection.fraud_detection_service.service;

import com.example.fraud_detection.fraud_detection_service.repository.FraudAnalysisResultRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.fraud_detection.fraud_detection_service.dto.FraudAnalysisResultRequestDto;
import com.example.fraud_detection.fraud_detection_service.dto.TransactionRequestDto;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.fraud_detection_service.mapper.FraudAnalysisResultMapper;




public class FraudanalysisResultService {
    

    private final FraudAnalysisResultRepository fraudAnalysisResultRepository;
    private final FraudAnalysisResultMapper fraudAnalysisResultMapper;

    public FraudanalysisResultService(FraudAnalysisResultRepository fraudAnalysisResultRepository,
                                     FraudAnalysisResultMapper fraudAnalysisResultMapper){
        this.fraudAnalysisResultRepository = fraudAnalysisResultRepository;
        this.fraudAnalysisResultMapper = fraudAnalysisResultMapper;
    }

    //this is for the dashboard. jus 10 at atime
    public Page<TransactionRequestDto> getLatestFraudAnalysisResults() throws Exception{
        Pageable latestFirst = PageRequest.of(0, 10, Sort.by("createdAt").descending());
         Page<FraudAnalysisResult> dashBoardTransactionPage = fraudAnalysisResultRepository.findAll(latestFirst);


        return dashBoardTransactionPage.map(analysisResult -> {
                try {
                    FraudAnalysisResultRequestDto dto =
                        fraudAnalysisResultMapper
                            .FraudAnalysisResultToDtoMapper(analysisResult);

                    return dto.transactionRequestDto();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to map fraud analysis result", e);
                }
        });           

    }


    public Page<FraudAnalysisResultRequestDto> getAnalsisResultsInPages(int page) throws Exception{
        Pageable currentPage = PageRequest.of(page  -1, 10);

        // return fraudAnalysisResultRepository.findAll(currentPage)
        //         .map(analysisResult ->
        //               fraudAnalysisResultMapper
        //                     .FraudAnalysisResultToDtoMapper(analysisResult));

                
        return fraudAnalysisResultRepository.findAll(currentPage).map(result -> {
                try {
                    return fraudAnalysisResultMapper.FraudAnalysisResultToDtoMapper(result);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to map fraud analysis result", e);
                }
            });
        
   
     }


}
