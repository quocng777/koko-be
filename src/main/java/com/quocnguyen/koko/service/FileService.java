package com.quocnguyen.koko.service;


import com.quocnguyen.koko.dto.AttachmentPreSingedUrlDTO;

/**
 * @author Quoc Nguyen on {2024-08-18}
 */
public interface FileService {

    AttachmentPreSingedUrlDTO generatePreSignedUrl();
}
