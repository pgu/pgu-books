package pgu.shared.domain;

import java.util.ArrayList;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ImportResult implements IsSerializable {

    @Id
    private Long              id;
    private int               start;
    private int               length;
    private ArrayList<String> misseds;
    private String            importDate;
    private boolean           done;
    private String            lastBook;
    private int               lastLineNb;
    private boolean           lastImport;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImportResult other = (ImportResult) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ImportResult [id=" + id + ", start=" + start + ", length=" + length + ", misseds=" + misseds
                + ", importDate=" + importDate + ", done=" + done + ", lastBook=" + lastBook + ", lastLineNb="
                + lastLineNb + ", lastImport=" + lastImport + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(final int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(final String importDate) {
        this.importDate = importDate;
    }

    public ArrayList<String> getMisseds() {
        return misseds;
    }

    public void setMisseds(final ArrayList<String> misseds) {
        this.misseds = misseds;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public String getLastBook() {
        return lastBook;
    }

    public void setLastBook(final String lastBook) {
        this.lastBook = lastBook;
    }

    public void setLastLineNb(final int lastLineNb) {
        this.lastLineNb = lastLineNb;
    }

    public int getLastLineNb() {
        return lastLineNb;
    }

    public boolean isLastImport() {
        return lastImport;
    }

    public void setLastImport(final boolean lastImport) {
        this.lastImport = lastImport;
    }

}
