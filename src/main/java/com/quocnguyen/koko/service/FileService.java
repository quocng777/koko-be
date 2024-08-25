package com.quocnguyen.koko.service;


import com.quocnguyen.koko.dto.AttachmentPreSingedUrlDTO;
import com.quocnguyen.koko.model.Attachment;

import java.util.Collection;

/**
 * @author Quoc Nguyen on {2024-08-18}
 */
public interface FileService {

    AttachmentPreSingedUrlDTO generatePreSignedUrl();

    void deleteFiles(String[] keys);
}
