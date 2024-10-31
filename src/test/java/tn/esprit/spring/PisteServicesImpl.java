package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllPistes() {
        // Arrange
        Piste piste1 = new Piste();
        Piste piste2 = new Piste();
        List<Piste> pistes = Arrays.asList(piste1, piste2);

        when(pisteRepository.findAll()).thenReturn(pistes);

        // Act
        List<Piste> result = pisteService.retrieveAllPistes();

        // Assert
        assertEquals(2, result.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        // Arrange
        Piste piste = new Piste();
        when(pisteRepository.save(piste)).thenReturn(piste);

        // Act
        Piste result = pisteService.addPiste(piste);

        // Assert
        assertEquals(piste, result);
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRemovePiste() {
        // Arrange
        Long numPiste = 1L;

        // Act
        pisteService.removePiste(numPiste);

        // Assert
        verify(pisteRepository, times(1)).deleteById(numPiste);
    }

    @Test
    void testRetrievePiste() {
        // Arrange
        Long numPiste = 1L;
        Piste piste = new Piste();
        piste.setNumPiste(numPiste);

        when(pisteRepository.findById(numPiste)).thenReturn(Optional.of(piste));

        // Act
        Piste result = pisteService.retrievePiste(numPiste);

        // Assert
        assertEquals(numPiste, result.getNumPiste());
        verify(pisteRepository, times(1)).findById(numPiste);
    }
    // Ce test vérifie que le service peut supprimer une piste non existante sans erreur

    @Test
    void testRemoveNonExistentPiste() {
        // Arrange
        Long invalidPisteId = -1L; // Utiliser un ID invalide

        // Act
        pisteService.removePiste(invalidPisteId);

        // Assert
        verify(pisteRepository, times(1)).deleteById(invalidPisteId);
    }
    @Test
    void testAddPisteWithSpecificLength() {
        // Arrange
        Piste piste = new Piste();
        piste.setNamePiste("Piste Test");
        piste.setLength(500); // Longueur spécifique à tester

        when(pisteRepository.save(piste)).thenReturn(piste);

        // Act
        Piste result = pisteService.addPiste(piste);

        // Assert
        assertEquals(500, result.getLength());
        verify(pisteRepository, times(1)).save(piste);
    }
    @Test
    void testRetrieveAllPistesWithLargeDataset() {
        // Arrange
        List<Piste> largeDataset = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Piste piste = new Piste();
            piste.setNamePiste("Piste " + i);
            largeDataset.add(piste);
        }
        when(pisteRepository.findAll()).thenReturn(largeDataset);

        // Act
        List<Piste> result = pisteService.retrieveAllPistes();

        // Assert
        assertEquals(1000, result.size());
        verify(pisteRepository, times(1)).findAll();
    }
    @Test
    void testRetrieveNonExistentPisteReturnsNull() {
        // Arrange
        Long nonExistentPisteId = 99L;
        when(pisteRepository.findById(nonExistentPisteId)).thenReturn(Optional.empty());

        // Act
        Piste result = pisteService.retrievePiste(nonExistentPisteId);

        // Assert
        assertNull(result);
        verify(pisteRepository, times(1)).findById(nonExistentPisteId);
    }


}
