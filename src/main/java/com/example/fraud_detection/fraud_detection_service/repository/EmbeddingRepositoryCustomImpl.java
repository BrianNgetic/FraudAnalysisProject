package com.example.fraud_detection.fraud_detection_service.repository;

import com.example.fraud_detection.fraud_detection_service.entity.TransactionEmbedding;
import com.example.fraud_detection.fraud_detection_service.entity.SimilarTransactionDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingRepositoryCustomImpl
        implements EmbeddingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

   @Override
public List<SimilarTransactionDTO> findSimilar(
        float[] embedding,
        int limit
) {
    if (embedding == null || embedding.length == 0) {
        throw new IllegalArgumentException(
                "embedding must not be null or empty"
        );
    }

    if (limit <= 0) {
        throw new IllegalArgumentException(
                "limit must be greater than zero"
        );
    }

    String vector = toPgVectorLiteral(embedding);

   String sql = """
    SELECT
        te.*,
        te.embedding <=> CAST(:embedding AS vector) AS distance,
        1 - (te.embedding <=> CAST(:embedding AS vector)) AS similarity
    FROM transaction_embeddings te
    ORDER BY te.embedding <=> CAST(:embedding AS vector)
    LIMIT :limit
    """;


    @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager
        .createNativeQuery(sql, "SimilarTransactionMapping")
        .setParameter("embedding", vector)
        .setParameter("limit", limit)
        .getResultList();

    return rows.stream()
        .map(row -> new SimilarTransactionDTO(
                (TransactionEmbedding) row[0],
                ((Number) row[1]).doubleValue(),
                ((Number) row[2]).doubleValue()
        ))
        .toList();
}

    private String toPgVectorLiteral(float[] embedding) {
        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < embedding.length; i++) {
            if (i > 0) {
                result.append(",");
            }

            result.append(Float.toString(embedding[i]));
        }

        return result.append("]").toString();
    }
}
