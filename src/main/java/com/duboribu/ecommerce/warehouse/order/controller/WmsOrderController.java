package com.duboribu.ecommerce.warehouse.order.controller;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.warehouse.order.dto.request.CreateDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.ProcessDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.request.SelectDeliveryRequest;
import com.duboribu.ecommerce.warehouse.order.dto.response.UpdateWmsOrderResponse;
import com.duboribu.ecommerce.warehouse.order.service.WmsOrderService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/wms/order")
public class WmsOrderController {
    private final WmsOrderService wmsOrderService;

    /**
     * 발주조회
     */
    @GetMapping("/list")
    public ResponseEntity<DefaultResponse> list(SelectDeliveryRequest request) {
        return new ResponseEntity<>(new DefaultResponse<>(wmsOrderService.list(request)), HttpStatus.OK);
    }
    /**
     * 발주등록
     * */
    @PostMapping("/register")
    public ResponseEntity registerOrder(CreateDeliveryRequest request) {
        if (!wmsOrderService.register(request)) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
    /**
     * 프로세스 진행
     * */
    @PostMapping("/process")
    public List<UpdateWmsOrderResponse> updateOrderStates(ProcessDeliveryRequest request) {
        return wmsOrderService.updateOrderStates(request);
    }
    /*
     * 리스트 바코드 확인
     * */
    @GetMapping("/barcode")
    public ResponseEntity<byte[]> qrToTistory(){

        String barcodeData = "test";
        byte[] barcodeBytes = null;

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeData, BarcodeFormat.CODE_128, 400, 200);
            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(barcodeImage, "png", baos);
            barcodeBytes = baos.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(barcodeBytes.length);

        return new ResponseEntity<>(barcodeBytes, headers, HttpStatus.OK);
    }

}
