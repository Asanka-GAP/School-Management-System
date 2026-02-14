package com.school.service;

import com.school.dto.BatchDTO;
import com.school.entity.Batch;
import com.school.enums.BatchType;
import com.school.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;

    @Transactional
    public BatchDTO create(BatchDTO dto) {
        Batch batch = Batch.builder()
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .build();
        return toDTO(batchRepository.save(batch));
    }

    @Transactional(readOnly = true)
    public BatchDTO getById(Long id) {
        return batchRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
    }

    @Transactional(readOnly = true)
    public List<BatchDTO> getAll() {
        return batchRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BatchDTO> getByType(BatchType type) {
        return batchRepository.findByType(type).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BatchDTO update(Long id, BatchDTO dto) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        batch.setName(dto.getName());
        batch.setType(dto.getType());
        batch.setDescription(dto.getDescription());
        return toDTO(batchRepository.save(batch));
    }

    @Transactional
    public void delete(Long id) {
        batchRepository.deleteById(id);
    }

    private BatchDTO toDTO(Batch batch) {
        return BatchDTO.builder()
                .id(batch.getId())
                .name(batch.getName())
                .type(batch.getType())
                .description(batch.getDescription())
                .build();
    }
}
