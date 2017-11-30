import myutils.HtmlUtils
import org.scalatestplus.play.PlaySpec

class EscapeSpec extends PlaySpec {
  "HtmlUtils" should {
    "correctly safe-escape HTML" in {
      val unsafeNode =
        <b>
          Good
          <i>
            Good nested
            <u>
              Good nested 2
              <a href="http://website.com/page.html">
                Good nested 3
                <a src="...">
                  Bad nested 4
                </a>
              </a>
              <b src="???">
                Bad nested 3

                <i>
                  Good nested in bad
                </i>
                <script>
                  Bad nested in bad
                </script>
              </b>
            </u>

            <script>
              Bad nested 2
            </script>
          </i>
          <script src="text/javascript">
            Bad nested
          </script>
        </b>
      val safeNode =
        <b><i><u><a href="http://website.com/page.html"><span class="unsafe-html">&amp;lt;a src=&amp;quot;...&amp;quot;&amp;gt;
        Bad nested 4
        &amp;lt;/a&amp;gt;</span>
        </a>
        <span class="unsafe-html">&amp;lt;b src=&amp;quot;???&amp;quot;&amp;gt;
          Bad nested 3
          &amp;lt;i&amp;gt;
          Good nested in bad
          &amp;lt;/i&amp;gt;
          &amp;lt;script&amp;gt;
          Bad nested in bad
          &amp;lt;/script&amp;gt;
          &amp;lt;/b&amp;gt;</span>
        </u>
        <span class="unsafe-html">&amp;lt;script&amp;gt;
          Bad nested 2
          &amp;lt;/script&amp;gt;</span>
        </i>
        <span class="unsafe-html">&amp;lt;script src=&amp;quot;text/javascript&amp;quot;&amp;gt;
          Bad nested
          &amp;lt;/script&amp;gt;</span>
        </b>

      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "script") mustBe empty
      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "b" \ "@src") mustBe empty
      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "a" \ "@src") mustBe empty
      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "b") must not be empty
      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "i") must not be empty
      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "u") must not be empty
      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "a") must not be empty
      (HtmlUtils.escapeUnsafe(unsafeNode) \\ "a" \ "@href") must not be empty
    }
  }
}