set path=res/**,src/**
let mapleader=","
map <Leader>o :find 
map <Leader>m :!ant<Up>
map <Leader>p O/*<CR><ESC>:r!date +"\%Y \%B \%-d"<CR>I <LEFT><BS><ESC>
            \o<CR>The author disclaims copyright to this source code.<CR><BS>/<CR>
            \package com.appamatto.dhammapada;<ESC>
map <Leader>i oimport android.

command! Mkpath :!mkdir -p %:p:h
