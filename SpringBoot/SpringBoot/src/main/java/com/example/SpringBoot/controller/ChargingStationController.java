package com.example.SpringBoot.controller;

import com.example.SpringBoot.entities.ChargingStationData;
import com.example.SpringBoot.pdfgenerator.PDFGenerator;
import com.example.SpringBoot.queue.Producer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChargingStationController {

    List<ChargingStationData> chargingStationDataList = new ArrayList<ChargingStationData>(){
    {add(new ChargingStationData(1, 1, 1, new Date(1, 1, 1)));}
    {add(new ChargingStationData(1, 1, 1, new Date(1, 1, 1)));}
    };

    private final static String BROKER_URL = "tcp://localhost:6616";
    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/dist?user=admin&password=password";

    @GetMapping(value="/invoices/{customerId}", produces="application/json")
    public List<ChargingStationData> getCustomerChargingStationInfo(@PathVariable String customerId) throws Exception {
        List<ChargingStationData> chargingStationDataList = new ArrayList<>();

        try (Connection conn = connect()) {
            String sql = "SELECT * FROM chargingstationdata WHERE customer_id=" + customerId;
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ChargingStationData csd = new ChargingStationData();

                csd.stationId = resultSet.getInt("station_id");
                csd.customerId = resultSet.getInt("customer_id");
                csd.kwh = resultSet.getInt("kwh");
                csd.datetime = resultSet.getDate("datetime");

                chargingStationDataList.add(csd);
            }

        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PDFGenerator.generatePdf(chargingStationDataList);
        return chargingStationDataList;
    }

    @PostMapping("/invoices/{customerId}")
    public String postCustomerId(@PathVariable String customerId) throws Exception {

        Producer.send(customerId, BROKER_URL);

        getCustomerChargingStationInfo(customerId);
        return customerId;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}
