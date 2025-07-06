package org.ashirov.nicolai.pastebin.controller;

import org.ashirov.nicolai.pastebin.dao.PasteDao;
import org.ashirov.nicolai.pastebin.model.Paste;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pastebin")
public class PasteBinController {

    private final PasteDao pasteDao;

    @Autowired
    public PasteBinController(PasteDao pasteDao) {
        this.pasteDao = pasteDao;
    }

    @GetMapping("/{url}")
    public String getPasteBin(@PathVariable String url) {
        return pasteDao.getPaste(url);
    }

    @PostMapping
    public String insertPaste(@RequestBody Paste paste) {
        return pasteDao.insertPaste(paste);
    }

}
