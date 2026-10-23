/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.cvqc.workbench;

import static cn.zhuatech.cvqc.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);

    keys(r, "threshold", "samples");
    var threshold = decimal(r, "threshold", 0, 1);
    Set<String> ids = new HashSet<>();
    int tp = 0, fp = 0, tn = 0, fn = 0;
    List<Map<String, Object>> out = new ArrayList<>();
    for (var s : rows(r, "samples", 1, 500)) {
      keys(s, "id", "actualDefect", "defectProbability");
      String id = id(s, "id");
      unique(ids, id);
      boolean actual = flag(s, "actualDefect"),
          predicted = decimal(s, "defectProbability", 0, 1).compareTo(threshold) >= 0;
      String type = actual ? predicted ? "TP" : "FN" : predicted ? "FP" : "TN";
      switch (type) {
        case "TP" -> tp++;
        case "FP" -> fp++;
        case "TN" -> tn++;
        default -> fn++;
      }
      out.add(Map.of("id", id, "predictedDefect", predicted, "outcome", type));
    }
    List<Map<String, Object>> metrics = new ArrayList<>();
    metrics.add(ratio("precision", tp, tp + fp));
    metrics.add(ratio("recall", tp, tp + fn));
    metrics.add(ratio("specificity", tn, tn + fp));
    metrics.add(ratio("accuracy", tp + tn, out.size()));
    return Map.of(
        "truePositive",
        tp,
        "falsePositive",
        fp,
        "trueNegative",
        tn,
        "falseNegative",
        fn,
        "metrics",
        metrics,
        "samples",
        out);
  }
}
