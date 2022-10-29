package br.com.projetoAplicadoIV.site.service;

import br.com.projetoAplicadoIV.site.entity.User;
import br.com.projetoAplicadoIV.site.entity.dto.NewUserDTO;
import br.com.projetoAplicadoIV.site.entity.dto.UpdateUserDTO;
import br.com.projetoAplicadoIV.site.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDataVerification userDataVerification;

    public UserService(UserRepository userRepository, UserDataVerification userDataVerification) {
        this.userRepository = userRepository;
        this.userDataVerification = userDataVerification;
    }

    public String saveUser(NewUserDTO newUser) {
        if(userDataVerification.checkEmptyFields(newUser)) {
            return userDataVerification.getMessage();
        } else {
            if(checkForUserByCPF(newUser.getCpf()).isPresent() || checkForUserByEmail(newUser.getEmail()).isPresent()) {
                return "User already exists.";
            }

            User user = new User();
            user.setCpf(newUser.getCpf());
            user.setEmail(newUser.getEmail());
            user.setName(newUser.getName());
            user.setPassword(newUser.getPassword());

            userRepository.save(user);
        }
        return "Success";
    }

    public String updateUser(UpdateUserDTO user, String cpf) {
        Optional<User> existingUser = checkForUserByCPF(cpf);

        if(existingUser.isPresent()) {
            User usr = existingUser.get();

            if(userDataVerification.validateUpdate(user)) {
                return userDataVerification.getMessage();
            }
            userRepository.save(usr);
            return "User updated successfully.";
        }
        return "User with CPF '" + cpf + "' does not exist.";
    }

    public String deleteUser(String cpf) {
        Optional<User> user = checkForUserByCPF(cpf);

        if(user.isPresent()) {
            userRepository.delete(user.get());
            return "User deleted successfully.";
        }

        return "User with CPF " + cpf + " does not exist.";
    }

    public Optional<User> checkForUserByCPF(String cpf) {
        return userRepository.findByCPF(cpf);
    }

    public Optional<User> checkForUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
