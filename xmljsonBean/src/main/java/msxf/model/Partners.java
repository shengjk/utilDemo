package xxx.model;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by landscape.
 */
public class Partners {
// 隐式集合，加上这个注解可以去掉book集合最外面的<partnerList></partnerList>这样的标记
    @XStreamImplicit
    private List<Partner> partnerList;

    public List<Partner> getPartnerList() {
        return partnerList;
    }

    public void setPartnerList(List<Partner> partnerList) {
        this.partnerList = partnerList;
    }
}
