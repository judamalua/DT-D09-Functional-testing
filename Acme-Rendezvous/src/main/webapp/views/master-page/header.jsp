<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class = "crop">
	<a href="welcome/index.do"> <img class="banner img-responsive" src="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxISEhUSEhIVFRUWFRUVFRUVFRUXFRcWFRUXFxYVFxUYHSggGBolGxUYITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGhAQFy0dHh0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLTctNy0tLTctLS03Lf/AABEIALcBEwMBIgACEQEDEQH/xAAcAAACAgMBAQAAAAAAAAAAAAADBAIFAAEGBwj/xAA7EAABAwIEBAQFBAAFAwUAAAABAAIRAyEEEjFBBVFhcQYTIoEykaGx8ELB0eEHFCNS8TNykhUWJENi/8QAGAEAAwEBAAAAAAAAAAAAAAAAAAECAwT/xAAfEQEBAQEAAgMBAQEAAAAAAAAAARECITEDEkFRE3H/2gAMAwEAAhEDEQA/AOllaWKdKnK5K31oKYoynaGETbcOj0RDD0FYUhC35YCkAp02ysAU8iyE9EaBWZ1KEMtQeJeYpCohBq2jSwQvQ3VFpyA8I0Y296GVGEWm1EFYxpTDGrbGqeZXC1sBYQtB9pWmvlGhp0qIlbLpmNgtsIhGkG8wg+amaoCSqBK08Mh4K3lCWplMAolGN5AoOwwKk5yj5ielgVXBgpKrw1vIJ81lA1ktg+pH/wBLHJafwoclYtrI7agU+CvDna3CuiRq8PIXYOgperhgUfWI6+Nx5w55LF0pwQW1H1T/AJEaQkwrbB4ZIcMpTddDQpLZuxlKFj0VxQnIoBIU2NUg1EY1TOQwBRcEcIbgn0AoWQpgLagBEKMKblHMhWNZVBjZMb9bKTndxHMKWGcSNB3/AIKZINog621EfRSdTYRAMQbmbpjJaxvGm/zUGUhN9Y/vRPSwLMBE9RI27lTpAcxMDe6i4tb7n9llSqGjk6PufsjQxjbEX5gdJjdCe8MNgTJAj6jKBMomaSHcxqNeyL5TrCRM8ttr9kvNPIEC7TQu2F43UqbGZYaJtMzf5nqiVKQF5uRE6nqRGiTZhItLgNS5zfinsbap+YLI3SzGx1GsGb90F7Vrij3tAFHKSSAQLH7omCIc3WSLH2SoiLKamAmMkITwlqgiouRUOqUaks8qLQiELRdCSkSYWvNUC5RIRAZp10UVkhmhbNRMHvNWKt8xYkWLLh1GAFbjRI4MJ2VrE1FygVNyiAihkqbSorbUBMlQLljigvcl1TgsrRKB5i3nUaeJOK15ZKynJ/vRSNYs1gD5hVzCpd1QtlpAjQX9olSZix8Bb/4x9QoYgEg/DlN5MwIvohOoAiXm14c02HsTfsou/hyHGUA2SM3vNvcbIzGuOoy8iEOlWa2AJ5T2ExdEZVmQOe8K5hVp+Hbefre6i6mHCSAY6z3UiDeAfbcwoOMWuXHa/TYJ+P4TDSDACbASRv8Amv0RWVZFrEjfXuTsgOMhwd6RBkjlpugOxAu2kQebidovdHgC4nHMpN1zHf8ApLVsY1wh5IkSL78p/hUONweIzFxy5G3EuaLXMmSOmtlLA+JWuGQhsne0DuBup08NPwwpsmXh4Ng4jNyaORHZZhcU6m6TZromQIExFwYHZKDFZSXV61I7RziTB23+i5vH8TzVPiIZ+kbRzvunyV16a14IsZQ3BVfh3Fl7YJn/AGnaAro0H7NJS658+DlKuS7inamFqf7ClThqk/A75Kbzf4ewKEvWun24WoTAY75R91Kvw4M/6tRjOhMn5InNv4LYqJRpTP8A8QG+InsAs8zB71j9Ff8AnS+xB4QXOT1SnQd/0qwPQ/aUrkjVF5sOXQpWKZphaUm6LDJkFJ0TZHzqpUCEolOjmEyAli9Icazmn/pkyDsqlml5XRoHYg+6G+m4bLzTE8XxFP8AU75lTwPjOsz9U9HXRby0nx9XzHomVx0BK07DVNmlVfAPGdGv6S4MfyJsey6P/ORqqnHNRdl8kBw2odYHuijh53cE0caOa2cW3un/AJ8ltV9aKfX6eyrRjajg4+V6R1uSLwAn+MPlhgGY0Gqzwzgj5WZ981xzjnKmc7cG5HO1MZVLvU0M0dJk/QJpnEGy3MfVHpEt9XIRe877SrTG0KZcW2lV2J4AWAVA0P8AVLWXB1+oWd5sVLFjg3ktDnNLSf8AcQSNNI6p9rT0ntdVOAY6PUMp3EyB2V3R0H0nXbcK+Im1pwP9eyDWbGgknUdE5UZ+/W4S+IMcvyNev9LS8plVlZ2aw03Bn85qoxNelT+AhpBkBolukAz0CNj67Z1jUzvERAAF9JVHxHEtLZL53LLtuJLbzEnWOiysaSoYnGvZTzvr55dcBwN3GACJmfsqHHCvUaKrCxoE6Wm52OuuyBjq7nPkNaxzJMCQXE7um09eqLhmuyt8wzcn3MJfU9KYfClzpvpcmSfn3Un4bK4l1x1E/XZWAxlNhyucGzzMbpkhlQS1wMg3EyrnPhFvk54ZxTZgQxvObDnC9M4dVZliZheHMxnlAAH1a+y7vwb4j8wOY+BlAunz/Ts8PQDVGyhXxbGAlxAA1JXM8X8TU6LCQRI6heaeJvGNTENLQ7K02tv35K71hc/Ha6rxV/iWGk0sKQdjU5c4lcJiuMVHy59VxncrnTSB3KboUQVldtdHM54no+MaT8LpTGGqvduVHA4MDZdDwvhuY6d0fXBfk38b4RhXuMzA5rqA6yFSwwaICJEKL3rPESViksU6F1RNlNzkIGETMFUidaD1NpUDCC55CMwinF+ENqAkASuNxvDQ2Zbdd+KqXxWFZUFxfmqnX9OPMXU8jgWi433VvgfFtZnpccw6kq4xnhom7TKoeIeHnsuWH9k17L7dXw7xSx+9+Seq8bAsF5gcM9psCOoTP+bqAABxMc09pfWPSGcaBOQRexJ0vzW/EfEKzaTMPg7vdYu0DB2XDcIxDzUb5tmki4/deg4GlNQkNjS/NOdecR1M8kuD+CarCyq/GVjUBDnAOBYXbjKdR0Vu012F4cPhc3KZkEHWOX9q5okpOpXcQ/NZp+H+/or6kiZbQi7M4W1v00VlQjT8KRwLN9eo/b5fRWlChz1/LI5hWoOFv4SeNECRYX9o5DmrQsCVxTJEc1VidcPjrutz3uSNIt/Wy5jjsQbgkEGLT3BHKV3GL4dB1sSL5c3TSJ0tb+Fx3iXCubDssgk+oaG+lvfqs8aa53DmDcfzpsU9QzPDhpqIGsfslsNlDoBnTlHbunsJIdbQz35/yr5k/U2gO4Fh3CHMBgb6j3U6GHbhmuMuLCIykyPYq2DmamIj3VP4griA3mb6Qf4R1YJtV+Iw4cMzNOZ1jkqx/GjSJFMHNoTMK7wLQ6GEgE2E6HonMV4CrAktDCDsDH3WP28tXGVsVXrfE63JLtw7hY7rrf8A2lim/wD1nXoUxQ8H13n1Njuns/q9mOTpYQnqrzhfC3uNmkrtuG+EKbIz3PIaK+oYZlMQ1oCm9yeittctw7w24Q6pborylSDbAQAm6z0q5ZddWlEilazkWo6yUqFSodtRYtMoOImFieJXVWogGuh1KqCHq9LDjaqwlCKnRYSUAQMRaWGJTNCjCPmAVSJ0FtGEY0wRcAqEojXKoCOJ4JRfqwDskj4Woa3V8CscmSoo8BoNvkk8ymKfpqWnTTYdSnMqQfULnQ20au/ZTbhzyeOKygSb/coFNznuNraAHrv2SVQyWbjU9Vb4RwJEfkWVbpelnhKIaAANEcPVNxHjDaL207SQTcxyGu2olRbxBzmh1iDcEclts9J+nV8rlz/z3Qqirf8AOgjqN/4+SFi+KZWOOgDZmRbcBH2g+lErTOn99pHMqj4vQpva5oEEj4TYGBprZVNfx1TztaDnl2V221yPdw6a81rimIBuw6gWkx7dCN+gS2UrzZ7cfVwD2ucQDYm+u5M2003UWYryxPc63kd+iuK2ILXk7OkHpMXHuT81V8Ra1wmx52g9/wA5qbVSIUKxc3NNjMdiSR+yRxxLjY6CI+qm12UZfltdJgSxzybiAddJ0hQo7wV+aqKb5AkEO3H8hev7DsF5t4Tw+asxwdabE7R+lem2Ky79r5oDnoJfKZqU0tUaoxWt5lAuUXOUQ9Iq09LVESpVCA90p4QNRxNgrLB8MkAlF4bgB8R1VwIFlpzyVoTMI0BYjWW1eRDlnTC0xhRhdyKac6LNeo0iSrTC0wErhsPCfYFUibdbe9CcCj5Fjmpgt5kLYrqNVspWm0yp08WHmLXmoYaVppujaMNTZVeOexrgHSXGYAvE6kp9rrqr4lxBrBJBBdO1rWT69FDVNmZoIi1uqNhHFtQC/XpcfYBK4d8ZIMzMm0Sb/RWeHwwmdLtM9iFfJdPO/wDEJtZ9dwpHM6PgDhJa2Np5u/JXOeDfFVfA4g0MSH+U50ODgQaLtM+U6M0kdJXrHGPDAr1fMDzlMz6iCLgwyLXiDPJAPh+m0xlESJAJGYiLuOrtP1Sr9Vpz3sw2GAOJm2UnaNbDTTVeOf4neK3VKjsNSdDBZ5Fp3yW6QT3jmvZ6dTUGBbaQDmd9bgafRVFXw3h6p9dKmRv/AKdMAjmfT0Nz9VWz8O248K4OHEjNebCb9QL9Aey9Rwjy+m12sgEaQZ3ieqN4j8BYYFtSjSyO0cA52UiQRDf0jte26K7BCnTlpBBFg3rJmNrzbqUVj10o8W6T3J+QS1anGn5+FNijJJv7fb+lCuwRb5A9It0sFMn9PVPjBAPO+X+ELBsFWmWvj1bAeob/AMWW+LHufVeOicp4T/RZWYRGbKRvIE5voptGLrgdM4Z9IO0d8iNjC7kVlwp4mar6L6gALWwYEA3sYXXUjIn5LLtcWIqqLhKWpuTDCstMCrThIVHkK4ddJVcLJTgV0klW3DcBoSo4TAwVc0gArkK1IMACjkU6t9FFogK0oFy0oeUfyVinaFFSCsMJSSVC5VtREBEOixCmxQCJlVkwlRdKm1bASwFXiyE0J401DyUvqNCLZRW4ZaIhHp1FWEgKSoePYVtXI3MCQ6YFye66B9xExK5fG4EsqDI4klwmTAPS2gU9qi0bSDWjoSZ66K04bUzDvA99/uFQcSxzacUhcxFuauOCVswcOQJ30/IVfHfKelo+sG2tH2PXkqTiWNAkh2xtM/hmLdei5XiviepTe5tSjVaGn4oblc2ziZJED62XnnHvHFapLaTcgP63GX2nYWFiOa1+8vo58fUem/8Aq1LzizzYcR6ZLTm9QvljoP8AyKbfxZjBLTvbSwm1yOYGsROq+en1ahdmL35rSZM20v0XRcB8T12uDKmaqzSLZ76XOvqjVL7YrLXrlXjLnjT6N7bfn0SVZ2ckxE66RJmTAsPZcePF2GafV5ofu3JJ0uCZg/PZKYjxw4PaGUPRYepxzbXMCOdv+E70j6upFONYtb5mNO8JTFsy3J/AnsLUD6YeRE3+sqp4zirW/Oaju/g5nlUQXWO5T9HDESXAAGIIuLCJ7qupvcXGBpefkrXDYnWmdAwls/qMzHsp+ysTZ8TY1t913PD2/wCmIXENbYOvr/x9113h/ES3Kp6mwS+VlSaiuQ31IQHVlhWkmm2OR6UKvZVTNCoiUWLCgLopKGwLACFszTe/YKNZ8RzRNdFFtPcqiQD1i0/EtBgraXgKzB07J+FA4gBzgBp0tfcfmyx+IsM3pEXjY6TPKyrIWmqYtPJbcUpUqEHI43IkdYiYPO+iKH+mWkH1DNtAFyn4KVsm4HPfa3/KJh7yNwh/5hhJbJ52GxIED3IWUqnqgB3psZ1S8GKH3jpK2x0qFZrtZvy17CewKiysPhFybgaQLT7pkOWQgvKmQeduXJRc08rmw63hFgaplJ4hmZwtYGXdbHQpxguRuEpxKtAiNbduanpUcpxziLM4dlgDQcybBdDwfGZTSM6el3/adfkYXO8dwodUYJkkiBsOqhhq5a+A/NBvFxGkHqeSz56yr6mx2nivhYqUXnJmgSOvMdbSvJsRwLDn1EMaAW5hEOGaJsBcj9l7fg6wfSbJGg+eh16qk4lgqZnM1pvElomb+/52W/XOeYPj+T8teRVPC2FJy+Y5rshcIgj0uANj/wBw390mOCUm05biIJeacCneBULCT6piATI2XoOL4I0u9LO8RFzpy1H0VXi+EAasgHp++n4EQX/rj6fC2lxFMHL/ALjq4kiXdJiw2VlgfD4c4FwAg8leUME2bfxyTOLOVsDU/ZO2RO/wviSAAxpsBtuQqTiMAH5BPOdr91Q8SxErLf1Ug/CW3MmxBE9Y0+qnjDEEidCSNQh8OYQHAgzqIvEEQU2G+ok6EEHbkbJTyB2EhoEmDeT3sFdcKxZYQQucZiM0RsAD1VhhKq15Z9O1rvzAOahBpSPBsQZym4KuskLD5OfK+OgA1NYcXUIRKNisp7XVpRKnVKFRK2HiV0b4ZC0moOJqGcoQ6mIvAWmPGqW+MNvyG7rFA1O6xIa1QqOfrEAkToQd45/8pepXD7AQWG/I2uHAjTXraEJ2IEFpmREdrzpeZH5KLh6XqJmfhHe8nTS/8qt0sRxFdtXKRYMkGziD1IsRdOU/S3JaTqP1N5OjkYlRqUxOWJElpEQeUdyFt1I5mw0Q1oEX0H6ZGyPVBV9B3nANOWLmPUHTGhTBq5gQGvb6bOBiTa5m2/0WEZgRIvtpBkEQeYRaVJzaZlzg/cudMXkADQCAAiBISQC4+prYN4k7EaarbJaXFx0i8QLAbTtZCo4oktblIZLg50eouIgOG0TCJiquVukgQDl5X1v+Qjf0GPOaBckGYzWgzO22mvVCZEySTJsdjrYBVeHe/wAw5nSDcP1Hpk/YaKxoNh3qaLCZ0DjJk+0coUzq07zIfe39RbtFtOiVrUs1wDysNpRKGIuDmkElrbzoJv8AnJSfiSDlJAcTHQTMfQFXbExzfFuDuuQ2SGk35Doucpmo05BTOY6CIE7kzsF3tWoXVA1pkCCTrpr7bKk8TcBdVc14Pqn4pgZeQ6zssr59NJQMBxY04pPcA4zl0+Xf+05V4o6/fl8hA6/suG8U8IfhwS1xc62wglxNhBuLHXkqbB+L61MOFQtqQfS15yvuDIzDqI0tPZXz1SvM9vSG8VIkwD001uPl/PJV2JqOeR8vv9/uuNHjuY/0LRcF5Jm8Cco1kieaK7xy2wFGCQZ9WaCHRsBsJ90X7DHVmGCXGO/5qqXG4sEkk7/IbLmcZ4nfVJAMHafhF+XzSzatR13Ok6Wtp+ym6ci0xnEA6zT7oFDDl0/aFmGoaTrz7aSmq+IFME6yNP4Qr0kyplaZ5D+YhVlfiTn+logAyepWUK5cC9wuQYtsLapby/S2DBK055RasqbxN+misMDVnfdUuEqSHE7J3D1AYOipLp8JiIIPJdnw2sKrOq88pvNiF03AMVFpTqV7Vpwo0kWrVmyWaVzdTK1l0/50BaYZuq91S6bw9TZPSwYMWOFrLVWsN0tiMTAAanoWLHCNVipv9Q3lbRo8itpFrYaGkATlyieduR1MabJinWv6GyBEiDIuIc3p0sgzUc1zXZZA9MTDgeR/b/lboPJp5XEsIF/iBmJtO03PtEKiFw9U5nC8Cbgy4AA27jTtChVrODGhpBMzvNiLdrKdOWvLiRt7+kWPIzP5CUxjMwdlkObcAGxsTr1iLHVTaMFp0XXLg4CWkPAGV0klwM2BsL72T4LS0hstEiRuGm0xF5/NpHlsJdaIje4In/8AUKFWuXNLJjYnaJ/o6JyyCm6DmOENLiCS0G0mTOpE6lKYpgLmOYQ13wOAdAc0TLS02IvbcTbqGpXptc5rMzhIlwgAOIEwbeqCHdb7wp1sLDXEWsBc8iNudvp2RokIurOY9nljM3LJLfigVMr4H/aR8k4+r5YAzXktaTcTDjfkL69FDDMcWt8ss+MuIOWYMRc6WE+2oSPFK+UXIGYGQDpGgvfUAfOVN8Kk2rJlXJlB9RcHhpaRlGXLmNxOsfRP4YAzUOhBgAExynqVXPNMBjXA5mNzCJk+Y2XiBrbX2UX49zHNaRla6xdqYBABtafdG4WatKrg0uBZM7jS29tBqhVsRkbmJkw0XM63EDTkgOLgwucAf0gNkGJLRA+V76+6XGFe52arYGLSBoIieluWqVtOSfpqs+m6kRlDg/XeNBHMa69SvPvE/wDh4a9Rr6ALBDs7SRsAQWjmdI9132Gqtb6Wsyy71byBp3RzjiHwCHNzAE6XgD99Ec9DHz1juA1qTXOiQ2QbHSYmOSqMPXLSevSSvZfHFINLngehxLTe2YchsDH0XnT+E0nQR2ABt1cTqt+eipXBYQzLhbX+/qrE4hsl0E3gR9vopN4NULMwJNrA8hp80ozDkG+ukd/4U3k5Vx/mYN27C86HkqLE48uMPBOoHL2R8TWc5zWgyLWjXqteXnfBgZbpyFaLMU8u4E6bFZh2yzPyjXZTptcSdCP93Tkiw1rXAEaaKipSmL6fFcpuiyXRtsguMZdyBBCPhiZOyCW+EqXidFa4OvFwVR0W6HmrCiYQHb4bEZwI1RXLmeF40tN10dGqCFHc04Vc71Kzw7gAqvGQCITFyAssU3inEmy0CAJKyrXaLBaxVOG9SmExVndYkGYepGqxAXVZwe/0TOUanUCJk6g3ie/NZkz5WukuiLuNnRaI27zosWKkxOu2LkBpiLH9VhoLTY36bKueXS1wHq8yIBABbmGaeVyNFpYpqoec0ucWuMvYfSbATcAuicyBWaYALvW51mt0BsBJIvr9VixKg1Q4blJzODvie6BAzzFo2AsOyS43ifLplx1IJA2DZkW7Aj5LFifUyDn2LhcI91CnUOUBzMzhBBOZ03ymDqPrchVj+HsfXpl7zkaHEgCC55e3I3o2XzPRYsSvsT1VxWruio4RYQLAXAgTzOlz05KqxOEdV8l2cgMczPMbDYX3ge8rFiXuqkwccSeKT4u5rAXE65TGh56fLslMPWc9wxDz6cgY0CfS4/G+LCTEAAQJWLEEu+KAU2h2hn+b2CruL8ULaRtcOABGuafSOwWLE+hz5UP+IFSvUp0KRcGl1SmwNtq8Rc97nsqfAcDNEQ+HOnkNRy2HdYsV8XxC68LOIExJiFTYnCghzRGdxBLo6z+6xYtWalqcNc1wDj6jcEcuSrKx8urF79VixBnWSbzEjQLdLDyMu8ysWICBfBLn6jYJsCfUN1ixAO4Z8x0Vi02WLEAZlX5hXGAxhIElYsR0DtZ2YhM03rFixqwB6n2CeriSJ0WLFNCYqBYsWJB//9k="
		alt="Acme Co., Inc." />
	</a>
</div>

<div class="navbar">
	<a class="brand" href="#">&#160;&#160;Acme&#160;<img width = "24" src = "images/teamwork.png"/>&#160;Rendezvous</a>
	<div class="navbar-inner">
		<div class="container">
				

			<ul class="nav">
				<!-- id="jMenu" -->
				<!-- Do not forget the "fNiv" class for the first level links !! -->

				<security:authorize access="hasRole('USER')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><img src = "images/menu.png"/> <spring:message
								code="master.page.user" /><span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li class="arrow"></li>
							<li><a href="rendezvous/user/list.do"><spring:message
										code="master.page.createdRendezvous" /></a></li>

							<li><a href="announcement/user/list-created.do"><spring:message
										code="master.page.announcementsCreated" /></a></li>
							
						</ul></li>
							<li><a href="announcement/user/list.do"><spring:message
										code="master.page.announcements" /></a></li>
				</security:authorize>

				<security:authorize access="hasRole('ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><img src = "images/menu.png"/> <spring:message
								code="master.page.admin" /><span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li class="arrow"></li>
							<li><a href="actor/admin/register.do"><spring:message
										code="master.page.createAdmin" /></a></li>
							<li><a href="dashboard/admin/list.do"><spring:message
										code="master.page.dashboardList" /></a></li>
							<li><a href="configuration/admin/list.do"><spring:message
										code="master.page.configuration" /></a></li>
						</ul>
						
								
						</li>
				</security:authorize>

				<security:authorize access="isAnonymous()">
					<li><a class="fNiv" href="security/login.do"><img src = "images/login.png"/> <spring:message
								code="master.page.login" /></a></li>
					<li><a class="fNiv" href="actor/register.do"><img src = "images/register.png"/> <spring:message
								code="master.page.registerUser" /></a></li>
					<li><a class="fNiv" href="rendezvous/list.do?anonymous=true"><img src = "images/rendezvouses.png"/> <spring:message
								code="master.page.rendevouzList" /></a></li>
					<li><a class="fNiv" href="user/list.do?anonymous=true"><img src = "images/users.png"/> <spring:message
								code="master.page.userList" /></a></li>
								</ul>
				</security:authorize>

				<security:authorize access="isAuthenticated()">

					<li><a class="fNiv" href="rendezvous/list.do?anonymous=false"><img src = "images/rendezvouses.png"/> <spring:message
								code="master.page.rendevouzList" /></a></li>
					<li><a class="fNiv" href="user/list.do?anonymous=false"><img src = "images/users.png"/> <spring:message
								code="master.page.userList" /></a></li>
					
						</ul>
						<ul class = "nav pull-right">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><img src = "images/settings.png"/> <security:authentication
								property="principal.username" /><span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li class="arrow"></li>
							<security:authorize access="hasRole('ADMIN')">
							<li><a href="actor/admin/edit.do"><spring:message
										code="master.page.actorEdit" /></a></li>
							<li><a href="actor/display.do"><spring:message
										code="master.page.actorProfile" /></a></li>
							</security:authorize>
							<security:authorize access="hasRole('USER')">
							<li><a href="actor/user/edit.do"><spring:message
										code="master.page.actorEdit" /></a></li>
							<li><a href="user/display.do?anonymous=false"><spring:message
										code="master.page.actorProfile" /></a></li>
							</security:authorize>
							<li><a href="j_spring_security_logout"><spring:message
										code="master.page.logout" /> </a></li>
						</ul></li>
						</ul>
				</security:authorize>
			
		</div>
	</div>
</div>

<div class = "lang">
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

