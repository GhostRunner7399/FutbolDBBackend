package edu.uam.backend.cursos.User.Service;

import edu.uam.backend.cursos.User.Model.User;
import edu.uam.backend.cursos.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User crearUsuario(User user) {
        return userRepository.save(user);
    }

    public Optional<User> buscarUsuario(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> buscarUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> buscarUsuarioPorId(Long id) {
        return userRepository.findById(id);
    }

    public boolean eliminarUsuarioPorId(Long id) {
        Optional<User> user = buscarUsuarioPorId(id);
        if(user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
