package mini.springframework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mini.springframework.aop.PointcutLocator;

/**
 * 封装DefaultAspect和Order的属性值
 *
 * @author daffupman
 * @since 2020/6/13
 */
@AllArgsConstructor
@Getter
public class AspectInfo {

    private int orderIndex;
    private DefaultAspect aspectObject;
    private PointcutLocator pointcutLocator;
}
