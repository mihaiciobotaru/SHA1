package cripto.tema5;

import org.jetbrains.annotations.NotNull;

public class SHA1ss {

    static Long h0;
    static Long h1;
    static Long h2;
    static Long h3;
    static Long h4;

    public static @NotNull
    String generateDigest(String message){
        h0 = 0x67452301L & 0xffffffffL;
        h1 = 0xEFCDAB89L & 0xffffffffL;
        h2 = 0x98BADCFEL & 0xffffffffL;
        h3 = 0x10325476L & 0xffffffffL;
        h4 = 0xC3D2E1F0L & 0xffffffffL;

        ///converts message to binary and extend it until it is a multiple of 512
        StringBuilder binaryMessageSB = new StringBuilder(convertStringToBinary(message) + "1");
        String messageLength = Long.toBinaryString(binaryMessageSB.length()-1);
        Long a,b,c,d,e;
        long t,z,temp;

        //int x = (512 - (binaryMessageSB.length() + messageLength.length())%512);
        while(binaryMessageSB.length()%512 != 448){
            binaryMessageSB.append("0");
        }

        for(int i = 0 ; i < (binaryMessageSB.length() + messageLength.length() ) % 512; ++i){
            binaryMessageSB.append("0");
        }

        binaryMessageSB.append(messageLength);
        String binaryMessage = String.valueOf(binaryMessageSB);
        ///process the message chunk by chunk - a chunk is 512 bits
        for(int i = 0 ; i < binaryMessage.length()/512 ; ++i) {
            Long[] words = new Long[80];

            for(int j = 0 ; j < 16 ; ++j){
                StringBuilder tempSB = new StringBuilder();
                for(int k = 0 ; k < 32 ; ++k){
                    tempSB.append(binaryMessage.charAt(i * 512 + j * 32 + k));
                }
                words[j] = Long.parseLong(String.valueOf(tempSB),2) & 0xffffffffL;
            }


            ///creating new blocks of words
            for (int j = 16; j < 80; ++j) {
                words[j] =  leftrotate(words[j - 3] ^ words[j - 8] ^ words[j - 14] ^ words[j - 16], 1) & 0xffffffffL;
            }

            a = h0;
            b = h1;
            c = h2;
            d = h3;
            e = h4;

            for(int j = 0 ; j < 80 ; ++j){

                if(j < 20){
                    t = (b & c) | ((~b) & d);
                    t &= 0xffffffffL;
                    z = 0x5A827999L;
                }else if(j < 40){
                    t = b ^ c ^ d;
                    t &= 0xffffffffL;
                    z = 0x6ED9EBA1L;
                }else if(j < 60){
                    t = (b & c) | (b & d) | (c & d);
                    t &= 0xffffffffL;
                    z = 0x8F1BBCDCL;
                }else {
                    t = b ^ c ^ d;
                    t &= 0xffffffffL;
                    z = 0xCA62C1D6L;
                }
                temp = (leftrotate(a,5)) + t + z + e + words[j];
                temp &= 0xffffffffL;
                e = d;
                d = c;
                c = leftrotate(b,30) & 0xffffffffL;
                b = a;
                a = temp;


            }

            h0+=a;
            h1+=b;
            h2+=c;
            h3+=d;
            h4+=e;

            h0&=0xffffffffL;
            h1&=0xffffffffL;
            h2&=0xffffffffL;
            h3&=0xffffffffL;
            h4&=0xffffffffL;

        }

        String result = "";

        result = getString(result, h0);
        result = getString(result, h1);
        result = getString(result, h2);
        result = getString(result, h3);
        result = getString(result, h4);

        return result;

    }

    private static String getString(String result, Long hx) {
        String tempString;
        tempString = Long.toString(hx,16).toUpperCase();
        if(tempString.length() < 8) tempString = "0" + tempString;
        result += tempString + " ";

        return result;
    }


    public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Long.toBinaryString(aChar))
                            .replaceAll(" ", "0")
            );
        }
        return result.toString();

    }

    private static Long leftrotate(Long x, int shift) { //leftrotate function
        return ((x << shift) | (x >>> (32 - shift)));  //>>> is an UNSIGNED shift compared >> which is not
    }

    static public void main(String[] args){
        System.out.println(generateDigest("abc"));
        System.out.println(generateDigest("abC"));
        System.out.println(generateDigest("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"));
        System.out.println(generateDigest("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopQ"));

        ///1000 words text ->
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis ex dolor, varius vel mi vitae, pretium tincidunt ante. Vivamus a elementum lectus. Maecenas commodo ligula nisl, in interdum lectus vulputate ultricies. Vestibulum ut nunc congue, sodales quam quis, pellentesque massa. Donec blandit diam vel dolor convallis, sit amet maximus erat gravida. Aliquam vel ultrices justo. Pellentesque id elementum dui. Donec ut nibh tortor. Praesent lobortis gravida sollicitudin. Phasellus pharetra arcu vehicula libero bibendum, vel suscipit nisl commodo. Nam venenatis euismod erat, volutpat convallis elit elementum mattis. Pellentesque erat ipsum, maximus sed varius non, malesuada vel arcu. Sed fringilla, sem sit amet vulputate consectetur, libero leo hendrerit urna, et blandit tellus nibh quis nulla. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque non rutrum nunc. Aenean vitae sodales lacus. Praesent convallis nunc at orci tincidunt sodales. Pellentesque vehicula maximus porttitor. Cras vel justo malesuada, elementum ante non, posuere libero. Nam eleifend semper orci, a sagittis nunc venenatis ut. Morbi justo nunc, faucibus vitae felis at, maximus tempor orci. Mauris faucibus pulvinar nunc, a maximus nisi imperdiet sit amet. Fusce elementum eleifend consectetur. Aenean ex metus, lobortis at nisl ut, tristique tincidunt sapien. Proin eu leo facilisis est iaculis fermentum. Vestibulum at maximus nisi, sit amet interdum neque. Curabitur et tincidunt lectus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Morbi non purus a massa dapibus finibus. Aliquam a congue tortor. Donec ut facilisis mauris, vel sagittis sem. Nullam nec rutrum nulla. Phasellus vel enim purus. Morbi consequat massa vel porttitor facilisis. Praesent volutpat mi nec velit ullamcorper placerat. Etiam vel dictum est. Nullam imperdiet placerat nisi, sit amet faucibus nisl aliquet in. Sed congue dui nec lectus pharetra tincidunt. Vestibulum vitae dolor laoreet, porttitor odio non, rutrum metus. Ut quis nisl pulvinar, faucibus ex ut, ultricies quam. Maecenas quis lacus nibh. Phasellus hendrerit nisl sed vehicula aliquet. Nunc porta, lacus id dapibus iaculis, leo felis bibendum dolor, id maximus ipsum mi eu ligula. Praesent at ultricies turpis. Aenean eu quam eget nisl porta elementum in ut magna. Duis mattis justo quam, quis faucibus velit varius vitae. Vivamus volutpat varius nisl, vitae aliquam felis lobortis nec. Proin bibendum in lorem ac elementum. Integer finibus lacus non urna tristique, vitae venenatis felis tincidunt. Aenean luctus orci nec lacus volutpat, id posuere quam feugiat. Mauris sed elit sit amet odio pellentesque elementum. Aliquam erat nibh, auctor non dapibus quis, malesuada id nulla. Cras ultricies sapien et consectetur semper. Donec massa risus, condimentum sit amet pharetra et, gravida luctus nisi. In hac habitasse platea dictumst. Nam mattis ligula eu urna volutpat cursus. Nulla ac leo dictum, elementum felis vitae, hendrerit lacus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Quisque convallis, sem non suscipit elementum, orci lectus ornare quam, eget ornare odio dolor eu dui. Sed ullamcorper libero sapien, at commodo arcu pretium eget. Nam id placerat nibh, sit amet dignissim ex. Quisque id leo nec ante elementum pellentesque sit amet quis velit. Morbi maximus tincidunt quam ut consectetur. Maecenas consectetur porta commodo. Cras eu pretium dolor. Nullam vestibulum ipsum in neque lacinia, sit amet imperdiet diam congue. Cras sit amet pellentesque augue, eget pellentesque purus. Curabitur sed sodales arcu. Suspendisse ac ornare nibh, at tristique orci. Suspendisse gravida tempor quam vel cursus. Sed a enim eu ante efficitur rhoncus. Cras accumsan purus sit amet odio suscipit, quis condimentum erat condimentum. Phasellus molestie vitae lorem eu dapibus. Quisque et est vitae enim efficitur pretium sit amet ut orci. Maecenas porttitor eget urna eget sagittis. Nulla non turpis tempor, aliquam neque sit amet, cursus lorem. Integer auctor est ipsum, convallis consequat velit scelerisque eu. Cras sit amet lacinia nulla, commodo elementum leo. Nullam lobortis, odio eu efficitur vehicula, nisl ipsum accumsan erat, vitae aliquam nisl nulla nec nulla. Suspendisse potenti. Mauris pulvinar, purus a convallis scelerisque, nunc dui vehicula tellus, at convallis magna dui eget purus. Ut faucibus, ex ac bibendum lacinia, ante nunc convallis nulla, et mollis velit massa ac ante. Vivamus ac leo a lectus accumsan pellentesque eget id erat. Fusce mollis ultrices velit at dictum. Phasellus tortor enim, mattis non libero non, gravida consectetur dui. Curabitur a erat nec lorem semper sodales. Ut pulvinar dolor at interdum vehicula. Nam dapibus lacinia nulla quis ornare. Aliquam luctus erat nec purus molestie consectetur. Donec porta nulla et orci imperdiet, dignissim posuere diam posuere. Suspendisse vel nisi nec odio posuere maximus. Donec aliquet convallis neque, vitae varius diam dictum sit amet. Phasellus volutpat ullamcorper rhoncus. Nam blandit felis sit amet sem venenatis, id scelerisque nunc mattis. Duis consequat mi sed urna posuere, vitae tempor dolor varius. Sed sagittis, ipsum eget tempor egestas, erat nibh convallis ante, sit amet sagittis est elit blandit nulla. Praesent sed tellus in ex feugiat consectetur sit amet id dolor. Aliquam odio erat, porttitor sit amet lorem at, pulvinar pharetra turpis. Nam sit amet enim non tellus porta volutpat. Nunc tristique scelerisque dapibus. Cras massa justo, fermentum vehicula placerat at, consequat ac enim. Aenean egestas justo et velit tristique consectetur. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Morbi porttitor libero vulputate sodales tincidunt. Fusce pharetra, lacus ut gravida commodo, massa velit aliquam ligula, vitae consectetur ex erat vitae nibh. Nunc vitae elit aliquet libero tempor aliquam. Quisque et auctor nibh. Phasellus rutrum lobortis malesuada. In a sem consequat, feugiat odio id, blandit velit. Curabitur auctor semper est ac ornare. Fusce elementum diam ac ultrices commodo. Fusce sapien urna, sollicitudin nec luctus at, placerat vel tortor. Proin mattis commodo orci vel cursus. Donec id volutpat nisi. Integer ac accumsan risus. Duis nec elementum nibh. Praesent bibendum viverra lobortis. Ut et ligula ipsum. Maecenas lorem nunc, scelerisque id rhoncus ut, tincidunt a risus. Mauris id arcu a nibh consectetur vehicula. Morbi a nunc risus. Donec ac dolor id erat porta vehicula. Vivamus vitae enim sed risus mattis suscipit. Integer eu commodo diam, ornare iaculis lorem. Mauris eget orci id nisl tristique tempor. Praesent condimentum, metus ut fermentum semper.";
        System.out.println( '\n' + generateDigest(text + "a"));
        System.out.println(        generateDigest(text + "A"));
    }
}
