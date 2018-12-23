package org.codet.caseidilla.chat.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class NewDialogDto {
    private String participant;
    private boolean secret;
}
