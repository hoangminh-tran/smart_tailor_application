package com.smart.tailor.service.impl;

import com.smart.tailor.entities.PayOSData;
import com.smart.tailor.repository.PayOSDataRepository;
import com.smart.tailor.service.PayOSDataService;
import com.smart.tailor.utils.response.PayOSDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayOSDataServiceImpl implements PayOSDataService {
    private final PayOSDataRepository payOSDataRepository;

    @Transactional
    @Override
    public void save(PayOSData payOSData) {
        payOSDataRepository.save(payOSData);
    }

    @Override
    public Optional<PayOSData> findByOrderCode(Integer id) {
        return payOSDataRepository.findByOrderCode(id);
    }

    @Override
    public PayOSDataResponse getPayOSDataResponseByOrderCode(Integer code) {
        var checkPayOS = findByOrderCode(code);
        if (checkPayOS.isEmpty()) {
            return null;
        }
        var payOSData = checkPayOS.get();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return PayOSDataResponse
                .builder()
                .payOSID(payOSData.getPayOSID())
                .orderCode(payOSData.getOrderCode())
                .amount(payOSData.getAmount())
                .status(payOSData.getStatus())
                .checkoutUrl(payOSData.getCheckoutUrl())
                .qrCode(payOSData.getQrCode())
                .createDate(payOSData.getCreateDate().format(dateTimeFormatter))
                .lastModifiedDate(payOSData.getLastModifiedDate() != null ? payOSData.getLastModifiedDate().format(dateTimeFormatter) : null)
                .build();
    }
}
