<?php
/**
 * Copyright (c) 2008-2010 Rafael E. Espinosa Santiesteban
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

require_once('Core.php');

/**
 * Spanish Class
 *
 * Suffix removal of spanish terms. Porter's Stemming Algorithm (PSA) implementation.
 * Use example:
 * Spanish::Stemm([word]);
 *
 * @package     phpirsuite
 * @subpackage  lib
 * @category    stemmer
 * @author      Rafael E. Espinosa Santiesteban
 * @link        http://phpirsuite.blackbird.org
 */
class Spanish extends Core {

    /**
    * Stemm a word.
    *
    * Perform suffix removal of a word.
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function Stemm($word){
        self::$c = "[^aeiouáéíóúü]";
        self::$v = "[aeiouáéíóúü]";
        self::$C = "[^aeiouáéíóúü][^aeiouáéíóúü]*";
        self::$V = "[aeiouáéíóúü][aeiouáéíóúü]*";

        self::$word = strtolower($word);

        self::$arrWord = self::word2arr($word);

        self::defAllRegions($word);

        $wordAfter0 = self::stepZero($word);

        if ($wordAfter0 != $word) {
            self::defAllRegions($wordAfter0);
        }
        $wordAfter1 = self::stepOne($wordAfter0);

        if ($wordAfter0 != $wordAfter1) {
            self::defAllRegions($wordAfter1);
            $wordAfter2 = $wordAfter1;
        } else if ($wordAfter0 == $wordAfter1) {
            $wordAfter2 = self::stepTwoA($wordAfter1);
            if ($wordAfter2 == $wordAfter1){
                $wordAfter2 = self::stepTwoB($wordAfter1);
            }
        }

        if ($wordAfter1 != $wordAfter2) {
            self::defAllRegions($wordAfter2);
        }
        $wordAfter3 = self::stepThree($wordAfter2);

        return $wordAfter3;

    }

    /**
    * Define all regions
    *
    * Define R1, R2 and Rv regions on a word.
    *
    * @access   public
    * @param    string
    * @return   none
    */
    public static function defAllRegions($word) {

        list(self::$r1, self::$r2) = self::defStdR1R2($word);

        self::$rv = self::defEsRv($word);
        return;
    }

    /**
    * Define Rv region
    *
    * Define Rv regions on a spanish word.
    * Rv definition:
    * If the second letter is a consonant, RV is the region after the next following vowel,
    * or if the first two letters are vowels, RV is the region after the next consonant, and
    * otherwise (consonant-vowel case) RV is the region after the third letter. But RV is the end
    * of the word if these positions cannot be found.
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function defEsRv($word = FALSE){
        if (!(is_array($word))){
            $word = self::word2arr($word);
        }

        $rv = FALSE;
        if (self::isConsonant($word[1])){

            for ($i = 2; $i < count($word); $i++){

                if (self::isVowel($word[$i])){
                    $rv = self::concatArrWord($word, $i+1);
                    break;
                }
            }
        } else if ((self::isVowel($word[0])) && (self::isVowel($word[1]))) {
            for ($i = 2; $i < count($word); $i++){

                if (self::isConsonant($word[$i])){
                    $rv = self::concatArrWord($word, $i+1);
                    break;
                }
            }
        } else if ((self::isConsonant($word[0])) && (self::isVowel($word[1]))) {
            $rv = self::concatArrWord($word, 3);
        } else {
            $rv = count($word) - 1;
        }
        return $rv;
    }

    /**
    * Remove acute.
    *
    * Remove the characters with accents.
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function removeAcute($word){
        return str_replace(array('á','é','í','ó','ú'), array('a','e','i','o','u'), $word);
    }

    /**
    * Step 0 of PSA
    *
    * Remove attached pronouns
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function stepZero($word = FALSE) {

        $suffixes = "me$|se$|sela$|selo$|selas$|selos$|la$|le$|lo$|las$|les$|los$|nos$";
        $suff = self::suffixMatch(self::$rv, $suffixes);
        $presuffixes1 = "iéndo${suff}|ándo${suff}|ár${suff}|ér${suff}|ír${suff}";
        $presuffixes2 = "iendo${suff}|ando${suff}|ar${suff}|er${suff}|ir${suff}";
        $presuffixes3 = "uyendo${suff}|yendo${suff}";
        $presuff1 = self::suffixMatch(self::$rv, $presuffixes1);
        $presuff2 = self::suffixMatch(self::$rv, $presuffixes2);
        $presuff3 = self::suffixMatch(self::$rv, $presuffixes3);
        if ((($suff != FALSE) || ($suff != "")) &&
            (($presuff1 != FALSE) || ($presuff2 != FALSE) || ($presuff3 != FALSE))){
            return self::removeAcute(preg_replace("/${suff}$/iu", "", $word));
        }
        return $word;
    }

    /**
    * Step 1 of PSA
    *
    * Remove standard suffix
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function stepOne($word = FALSE){

        $suffixes = "anza$|anzas$|ico$|ica$|icos$|icas$|ismo$|ismos$|able$|ables$".
                        "|ible$|ibles$|ista$|istas$|oso$|osa$|osos$|osas$".
                        "|amiento$|amientos$|imiento$|imientos$";

        $suff = self::suffixMatch(self::$r2, $suffixes);

        if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "", $word);
        }

        $suffixes = "adora$|ador$|ación$|adoras$|adores$|aciones$|ante$|antes$|ancia$|ancias$";
        $suffixes2 = "ic(${suffixes})|(${suffixes})";
        $suff = self::suffixMatch(self::$r2, $suffixes2);
        if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "", $word);
        }

        $suffixes = "logía$|logías$";
        $suff = self::suffixMatch(self::$r2, $suffixes);
        if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "log", $word);
        }

        $suffixes = "ución$|uciones$";
        $suff = self::suffixMatch(self::$r2, $suffixes);
        if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "u", $word);
        }

        $suffixes = "encia$|encias$";
        $suff = self::suffixMatch(self::$r2, $suffixes);
        if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "ente", $word);
        }

        $suffixes = "amente$";
        $suffixes2 = "ativ(${suffixes})|iv(${suffixes})";
        $suffixes3 = "os(${suffixes})|ic(${suffixes})|ad(${suffixes})";
        $suff = self::suffixMatch(self::$r1, $suffixes);
        $suff2 = self::suffixMatch(self::$r2, $suffixes2);
        $suff3 = self::suffixMatch(self::$r2, $suffixes3);
        if (($suff3 != FALSE) || ($suff3 != "")){
            return preg_replace("/${suff3}$/iu", "", $word);
        } else if (($suff2 != FALSE) || ($suff2 != "")){
            return preg_replace("/${suff2}$/iu", "", $word);
        } else if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "", $word);
        }

        $suffixes = "mente$";
        $suffixes2 = "ante(${suffixes})|able(${suffixes})|ible(${suffixes})|(${suffixes})";
        $suff = self::suffixMatch(self::$r2, $suffixes2);
        if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "", $word);
        }

        $suffixes = "idad$|idades$";
        $suffixes2 = "abil(${suffixes})|ic(${suffixes})|iv(${suffixes})|(${suffixes})";
        $suff = self::suffixMatch(self::$r2, $suffixes2);
        if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "", $word);
        }

        $suffixes = "iva$|ivo$|ivas$|ivos$";
        $suffixes2 = "at(${suffixes})";
        $suff = self::suffixMatch(self::$r2, $suffixes);
        $suff2 = self::suffixMatch(self::$r2, $suffixes2);
        if (($suff2 != FALSE) || ($suff2 != "")){
            return preg_replace("/${suff2}$/iu", "", $word);
        } else if (($suff != FALSE) || ($suff != "")){
            return preg_replace("/${suff}$/iu", "", $word);
        }

        return $word;
    }

    /**
    * Step 2a of PSA
    *
    * Remove verb suffix beginning with y
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function stepTwoA($word = FALSE) {

        $suffixes = "ya$|ye$|yan$|yen$|yeron$|yendo$|yo$|yó$|yas$|yes$|yais$|yamos$";
        $suffixes2 = "u(${suffixes})";
        $suff = self::suffixMatch(self::$rv, $suffixes);
        $suff2 = self::suffixMatch(self::$rv, $suffixes2);
        $suff3 = self::suffixMatch($word, $suffixes2);
        if ((($suff != FALSE) || ($suff != "")) &&
        (($suff2 != FALSE) || ($suff2 != "") || ($suff3 != FALSE) || ($suff3 != ""))){
            return preg_replace("/${suff}$/iu", "", $word);
        }

        return $word;
    }

    /**
    * Step 2b of PSA
    *
    * Remove other verb suffix.
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function stepTwoB($word = FALSE) {

        $suffixes = "en$|es$|éis$|emos$";
        $suffixes2 = "gu(${suffixes})";
        $suffixes3 = "arían$|arías$|arán$|arás$|aríais$|aría$|aréis$|aríamos$|aremos$|".
                    "ará$|aré$|erían$|erías$|erán$|erás$|eríais$|ería$|eréis$|eríamos$|".
                    "eremos$|erá$|eré$|irían$|irías$|irán$|irás$|iríais$|iría$|iréis$|".
                    "iríamos$|iremos$|irá$|iré$|aba$|ada$|ida$|ía$|ara$|iera$|ad$|ed$|id$|".
                    "ase$|iese$|aste$|iste$|an$|aban$|ían$|aran$|ieran$|asen$|iesen$|aron$|".
                    "ieron$|ado$|ido$|ando$|iendo$|ió$|ar$|er$|ir$|as$|abas$|adas$|idas$|ías$|".
                    "aras$|ieras$|ases$|ieses$|ís$|áis$|abais$|íais$|arais$|ierais$|aseis$|".
                    "ieseis$|asteis$|isteis$|ados$|idos$|amos$|ábamos$|íamos$|imos$|áramos$|".
                    "iéramos$|iésemos$|ásemos$";
        $suff = self::suffixMatch(self::$rv, $suffixes);
        $suff2 = self::suffixMatch($word, $suffixes2);
        $suff3 = self::suffixMatch(self::$rv, $suffixes3);
        if (($suff3 != FALSE) || ($suff3 != "")) {
            return preg_replace("/${suff3}$/iu", "", $word);
        } else if (($suff2 != FALSE) || ($suff2 != "")){
            return preg_replace("/u${suff}$/iu", "", $word);
        } else if (($suff != FALSE) || ($suff != "")) {
            return preg_replace("/${suff}$/iu", "", $word);
        }

        return $word;
    }

    /**
    * Step 3 of PSA
    *
    * Remove residual suffix.
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function stepThree($word = FALSE){

        $suffixes = "os$|a$|o$|á$|í$|ó$";
        $suff = self::suffixMatch(self::$rv, $suffixes);

        if (($suff != FALSE) || ($suff != "")) {
            return self::removeAcute(preg_replace("/${suff}$/iu", "", $word));
        }

        $suffixes = "e$|é$";
        $suffixes2 = "ue$|ué$";
        $suffixes3 = "g({$suffixes2})";
        $suff = self::suffixMatch(self::$rv, $suffixes);
        $suff2 = self::suffixMatch(self::$rv, $suffixes2);
        $suff3 = self::suffixMatch($word, $suffixes3);
        if ((($suff2 != FALSE) || ($suff2 != "")) && (($suff3 != FALSE) || ($suff3 != ""))) {
            return self::removeAcute(preg_replace("/${suff2}$/iu", "", $word));
        } else if (($suff != FALSE) || ($suff != "")) {
            return self::removeAcute(preg_replace("/${suff}$/iu", "", $word));
        }

        return self::removeAcute($word);
    }
}
