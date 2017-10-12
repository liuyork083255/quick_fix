package com.sumscope.cdhplus.realtime.quickfixj.model;

import java.util.List;

/**
 * Created by liu.yang on 2017/8/23.
 */
public class PParsedMsg {
    private List<PParserResult> parser_result;

    public List<PParserResult> getParser_result() {
        return parser_result;
    }

    public void setParser_result(List<PParserResult> parser_result) {
        this.parser_result = parser_result;
    }
}
