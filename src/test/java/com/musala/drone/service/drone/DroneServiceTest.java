package com.musala.drone.service.drone;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.musala.drone.config.ModelMapperConfig;
import com.musala.drone.exception.InvalidDroneException;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.model.drone.DroneRequest;
import com.musala.drone.model.drone.DroneResponse;
import com.musala.drone.repository.drone.DroneRepository;

import static com.musala.drone.model.drone.Model.CRUISER;
import static com.musala.drone.model.drone.Model.HEAVY;
import static com.musala.drone.model.drone.Model.LIGHT;
import static com.musala.drone.model.drone.Model.MIDDLE;
import static com.musala.drone.model.drone.State.IDLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;
    private ModelMapper mapper = new ModelMapperConfig().modelMapper();
    private DroneService droneService;

    @BeforeEach
    void setUp() {
        droneService = new DroneService(droneRepository, mapper);
    }

    @Test
    void test_list_withEmptyList_thenRetrieveEmptyList() {
        when(droneRepository.findAll()).thenReturn(List.of());
        assertEquals(0, droneService.list().size());
    }

    @Test
    void test_list_withListOf3Drones_thenValidateMapping() {
        Drone drone1 = new Drone();
        drone1.setId(1L);
        drone1.setSerialNumber("1001");
        drone1.setModel(HEAVY);
        drone1.setBatteryPercentage(100.0);
        drone1.setState(IDLE);

        Drone drone2 = new Drone();
        drone2.setId(2L);
        drone2.setSerialNumber("1002");
        drone2.setModel(CRUISER);
        drone2.setBatteryPercentage(90.0);
        drone2.setState(IDLE);

        Drone drone3 = new Drone();
        drone3.setId(3L);
        drone3.setSerialNumber("1003");
        drone3.setModel(MIDDLE);
        drone3.setBatteryPercentage(80.0);
        drone3.setState(IDLE);

        Drone drone4 = new Drone();
        drone4.setId(4L);
        drone4.setSerialNumber("1004");
        drone4.setModel(LIGHT);
        drone4.setBatteryPercentage(70.0);
        drone4.setState(IDLE);

        when(droneRepository.findAll()).thenReturn(List.of(drone1, drone2, drone3, drone4));
        List<DroneResponse> droneResponses = droneService.list();
        assertEquals(4, droneResponses.size());

        DroneResponse droneResponse1 = droneResponses.get(0);
        assertEquals(1L, droneResponse1.getId());
        assertEquals("1001", droneResponse1.getSerialNumber());
        assertEquals(HEAVY, droneResponse1.getModel());
        assertEquals(100.0, droneResponse1.getBatteryPercentage());
        assertEquals(IDLE, droneResponse1.getState());

        DroneResponse droneResponse2 = droneResponses.get(1);
        assertEquals(2L, droneResponse2.getId());
        assertEquals("1002", droneResponse2.getSerialNumber());
        assertEquals(CRUISER, droneResponse2.getModel());
        assertEquals(90.0, droneResponse2.getBatteryPercentage());
        assertEquals(IDLE, droneResponse2.getState());

        DroneResponse droneResponse3 = droneResponses.get(2);
        assertEquals(3L, droneResponse3.getId());
        assertEquals("1003", droneResponse3.getSerialNumber());
        assertEquals(MIDDLE, droneResponse3.getModel());
        assertEquals(80.0, droneResponse3.getBatteryPercentage());
        assertEquals(IDLE, droneResponse3.getState());

        DroneResponse droneResponse4 = droneResponses.get(3);
        assertEquals(4L, droneResponse4.getId());
        assertEquals("1004", droneResponse4.getSerialNumber());
        assertEquals(LIGHT, droneResponse4.getModel());
        assertEquals(70.0, droneResponse4.getBatteryPercentage());
        assertEquals(IDLE, droneResponse4.getState());
    }

    @Test
    public void test_register_withValidRequest_thenCreated() {
        Drone expectedDrone = new Drone();
        expectedDrone.setSerialNumber("serial_number");
        expectedDrone.setModel(LIGHT);
        expectedDrone.setState(IDLE);
        expectedDrone.setMaxWeight(500.0);
        expectedDrone.setBatteryPercentage(100.0);

        Drone expectedDroneWithId = new Drone();
        mapper.map(expectedDrone, expectedDroneWithId);
        expectedDroneWithId.setId(1l);

        lenient().when(droneRepository.save(refEq(expectedDrone))).thenReturn(expectedDroneWithId);
        droneService.register(new DroneRequest("serial_number", 500.0, LIGHT));
    }

    @Test
    public void test_register_withSerialNumberMoreThan100Chars_throwsInvalidDroneException() {
        final String invalidSerialNumber = "serial_number_serial_number_serial_number_serial_" +
                "number_serial_number_serial_number_serial_number_serial_number";
        DroneRequest request = new DroneRequest(invalidSerialNumber, 500.0, LIGHT);
        InvalidDroneException invalidDroneException = assertThrows(InvalidDroneException.class,
                () -> droneService.register(request));
        assertEquals(request, invalidDroneException.getDroneRequest());
        assertTrue(invalidDroneException.getMessage().contains(invalidSerialNumber));
    }
}
