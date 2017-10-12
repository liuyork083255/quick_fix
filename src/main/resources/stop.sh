#!/bin/bash

PROCESSNUM=`ps -ef | grep com.sumscope.cdhplus.realtime.quickfixj.Start | grep -v grep | wc -l`

if [ $PROCESSNUM -gt 0 ]
then
    kill -s 15 `ps -ef | grep com.sumscope.cdhplus.realtime.quickfixj.Start | grep -v grep | awk '{print $2}'`
    echo "quickfixJ stop success"
else
    echo "quickfixJ is not running..."
fi