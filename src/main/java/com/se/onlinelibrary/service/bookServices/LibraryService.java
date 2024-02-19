package com.se.onlinelibrary.service.bookServices;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.onlinelibrary.bean.Library;

import java.util.List;

public interface LibraryService extends IService<Library> {
//    List<Library> findAllLibrary();
    Library getLibrary(int libraryId);
}
