package ru.alarh.security.crypto;

import java.util.Optional;

public interface Encrypter {

    Optional<String> sign(String msg);

}
