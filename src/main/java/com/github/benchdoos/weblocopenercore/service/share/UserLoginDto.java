package com.github.benchdoos.weblocopenercore.service.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginDto {

    private String countryName;

    private String selectedLanguage;

    private String applicationVersion;
}
