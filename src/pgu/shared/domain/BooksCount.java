package pgu.shared.domain;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Cached;

@Cached
public class BooksCount implements IsSerializable {

    @Id
    private Long    id;

    private int     count;
    private String  countDate;
    private boolean isLast;

    @Override
    public String toString() {
        return "BooksCount [id=" + id + ", count=" + count + ", countDate=" + countDate + ", isLast=" + isLast + "]";
    }

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
        final BooksCount other = (BooksCount) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(final String countDate) {
        this.countDate = countDate;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(final boolean isLast) {
        this.isLast = isLast;
    }

}
