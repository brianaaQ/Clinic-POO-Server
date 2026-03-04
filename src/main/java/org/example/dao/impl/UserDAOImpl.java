package org.example.dao.impl;

import org.example.dao.UserDAO;
import org.example.database.BaseRepository;
import org.example.entities.User;
import org.example.entities.Doctor;
import org.example.entities.Patient;

import java.util.List;
import java.util.Optional;
//CRUD
public class UserDAOImpl extends BaseRepository<User> implements UserDAO {

    //constructor
    public UserDAOImpl() {
        super(User.class);
    }

    //salveaza un utilizator in baza de date + returneaza id
    @Override
    public Long save(User user) {
        return super.save(user);
    }

    //cauta/afiseaza un utilizator
    @Override
    public Optional<User> findById(Long id) {
        return super.findById(id);
    }

    //cauta/ afiseaza pacienti/doctori dupa email
    @Override
    public Optional<User> findByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email = ?1";
        return executeSingleQuery(jpql, email);
    }

    //cauta/ afiseaza doctori/pacienti dupa email si nume
    @Override
    public Optional<User> findByEmailAndName(String email, String name) {
        // imparte numele in mai multe parti pentru a pelucra prenume+nume de familie si nume complet
        String[] nameParts = name.trim().split("\\s+");
        if (nameParts.length == 1) {
            // un singur nume dat - cauta dupa prenume sau numele de fam
            String jpql = "SELECT u FROM User u WHERE u.email = ?1 AND " +
                         "(LOWER(u.firstName) LIKE LOWER(?2) OR LOWER(u.lastName) LIKE LOWER(?2))";
            return executeSingleQuery(jpql, email, "%" + name.toLowerCase() + "%");
        } else {
            // mai multe nume date - cauta dupa prenume SI numele de familie
            String firstName = nameParts[0];
            String lastName = String.join(" ", java.util.Arrays.copyOfRange(nameParts, 1, nameParts.length));
            
            String jpql = "SELECT u FROM User u WHERE u.email = ?1 AND " +
                         "LOWER(u.firstName) = LOWER(?2) AND LOWER(u.lastName) = LOWER(?3)";
            return executeSingleQuery(jpql, email, firstName, lastName);
        }
    }

    //returneaza toti utilizatori
    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    //metoda de update
    @Override
    public void update(User user) {
        super.update(user);
    }

    //metoda de stergere
    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    //boolean pentru a veriifca daca exista un utilizator cu adresa respectiva de email
    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
} 