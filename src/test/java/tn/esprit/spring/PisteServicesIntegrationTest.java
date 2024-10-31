package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PisteServicesIntegrationTest {

    @Autowired
    private PisteServicesImpl pisteService;

    @Autowired
    private IPisteRepository pisteRepository;

    @Test
    void testAddAndRetrievePisteIntegration() {
        // Arrange
        Piste piste = new Piste();
        piste.setNamePiste("Test Piste");

        // Act
        Piste savedPiste = pisteService.addPiste(piste);
        Piste retrievedPiste = pisteService.retrievePiste(savedPiste.getNumPiste());

        // Assert
        assertNotNull(savedPiste);
        assertEquals("Test Piste", retrievedPiste.getNamePiste());

        // Clean up
        pisteRepository.delete(savedPiste);
    }
}
