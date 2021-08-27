package com.kkk.sbgtest.stock;

import lombok.Data;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;

@Data
public class StockUnit implements Serializable {
    private static final long serialVersionUID = -4865946674835353945L;

    int id;
    String date;
    int openValue = 0;
    int closeValue = 0;
    int highValue = 0;
    int lowValue = 0;
    int volume = 0;

    public void setAllData(Elements tds) {
        for (int i = 0; i < tds.size(); i++) {
            Element lTd = tds.get(i);
            if (lTd.select("span").text().equals("")) continue;

            switch (i) {
                case 0:
                    date = lTd.select("span").text();
                    break;
                case 1:
                    closeValue = Integer.parseInt(lTd.select("span").text().replaceAll(",", ""));
                    break;
                case 2:
                    break;
                case 3:
                    openValue = Integer.parseInt(lTd.select("span").text().replaceAll(",", ""));
                    break;
                case 4:
                    highValue = Integer.parseInt(lTd.select("span").text().replaceAll(",", ""));
                    break;
                case 5:
                    lowValue = Integer.parseInt(lTd.select("span").text().replaceAll(",", ""));
                    break;
                case 6:
                    volume = Integer.parseInt(lTd.select("span").text().replaceAll(",", ""));
                    break;
            }
        }

    }

    public void fixData(StockUnit fix) {
        if (openValue==0 || closeValue==0 || highValue==0 || lowValue==0 || volume==0) {
            if (fix != null) {
                this.openValue = fix.openValue;
                this.closeValue = fix.closeValue;
                this.highValue = fix.highValue;
                this.lowValue = fix.lowValue;
                this.volume = fix.volume;
            }
            else {
                this.openValue = 1;
                this.closeValue = 1;
                this.highValue = 1;
                this.lowValue = 1;
                this.volume = 1;
            }
        }
    }
}
