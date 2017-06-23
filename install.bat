@echo off
title install parent,ops-rate-tools,ops-common,ops-domain,ops-biz
set current_dir=%cd%
mvn clean install -pl parent,ops-rate-tools,ops-common,ops-domain,ops-biz
pause