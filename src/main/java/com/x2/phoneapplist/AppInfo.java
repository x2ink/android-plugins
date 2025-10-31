package com.x2.phoneapplist;

import androidx.annotation.NonNull;

public class AppInfo {
    private String Icon;
    private String Name;
    private String Package;
    private String Version;
    private String Path;
    private Integer Flags;
    private long Size;

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public Integer getFlags() {
        return Flags;
    }

    public void setFlags(Integer flags) {
        Flags = flags;
    }

    private long FirstDate;
    private long LastDate;

    @NonNull
    @Override
    public String toString() {
        return "AppInfo{" +
                "Icon='" + Icon + '\'' +
                ", Name='" + Name + '\'' +
                ", Package='" + Package + '\'' +
                ", Version='" + Version + '\'' +
                ", Path='" + Path + '\'' +
                ", Flags=" + Flags +
                ", Size=" + Size +
                ", FirstDate=" + FirstDate +
                ", LastDate=" + LastDate +
                '}';
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public long getFirstDate() {
        return FirstDate;
    }

    public void setFirstDate(long firstDate) {
        FirstDate = firstDate;
    }

    public long getLastDate() {
        return LastDate;
    }

    public void setLastDate(long lastDate) {
        LastDate = lastDate;
    }
}
