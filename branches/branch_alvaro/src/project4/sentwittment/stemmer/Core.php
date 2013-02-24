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

/**
 * Core Class
 *
 * Core methods for Porter's Stemming Algorithm (PSA) implementations.
 *
 * @package     phpirsuite
 * @subpackage  lib
 * @category    stemmer
 * @author      Rafael E. Espinosa Santiesteban
 * @link        http://phpirsuite.blackbird.org
 */
class Core {
    protected static $c;
    protected static $v;
    protected static $C;
    protected static $V;
    protected static $arrWord;
    protected static $word;
    protected static $r1 = FALSE;
    protected static $r2 = FALSE;
    protected static $rv = FALSE;

    /**
    * Define R1 and R2 by the standard way
    *
    * @access   public
    * @param    string
    * @return   string
    */
    public static function defStdR1R2($word = FALSE) {
        if (!(is_array($word))){
            $word = self::word2arr($word);
        }

        $r1 = FALSE;
        for ($i = 1; $i < count($word); $i++) {
            if ((self::isVowel($word[$i-1]) == TRUE) && (self::isConsonant($word[$i]) == TRUE)){
                $r1 = self::concatArrWord($word, $i+1);
                break;
            }
        }

        $r2 = FALSE;
        for ($i = $i+1; $i < count($word); $i++) {
            preg_match("/".self::$v.self::$c."/ui", $word[$i-1].$word[$i], $match);
            if ((count($match) == 1)){
                $r2 = self::concatArrWord($word, $i+1);
                break;
            }
        }

        return array($r1, $r2);
    }

    /**
    * Is consonant?
    *
    * Check if a character is a consonant.
    *
    * @access   public
    * @param    string
    * @return   boolean
    */
    public static function isConsonant($character) {
        preg_match("/".self::$c."/ui", $character, $match);

        if (count($match) >= 1)
            return TRUE;
        else
            return FALSE;
    }

    /**
    * Is vowel?
    *
    * Check if a character is a vowel.
    *
    * @access   public
    * @param    string
    * @return   boolean
    */
    public static function isVowel($character) {
        preg_match("/".self::$v."/ui", $character, $match);

        if (count($match) >= 1)
            return TRUE;
        else
            return FALSE;
    }

    /**
    * Concatenate Array Word
    *
    * Convert a word array.
    *
    * @access   public
    * @param    array
    * @param    integer
    * @return   mixed
    */
    public static function concatArrWord($arrWord, $start = 0){
        if ($start >= count($arrWord)) {
            return FALSE;
        }

        $word = "";
        for ($i = $start; $i < count($arrWord); $i++){
            $word .= $arrWord[$i];
        }
        return $word;
    }

    /**
    * Word to Array
    *
    * Convert a word on characters array.
    *
    * @access   public
    * @param    string
    * @return   array
    */
    public static function word2arr($word) {
        preg_match_all("/[a-zñáéíóúü]/ui", $word, $matches);
        return $matches[0];
    }

    /**
    * Suffix match?
    *
    * Check if suffixes match on word.
    *
    * @access   public
    * @param    string
    * @param    string
    * @return   mixed
    */
    public static function suffixMatch($word, $suffixes){
        preg_match("/".$suffixes."/ui", $word, $match);

        if (count($match) >= 1)
            return $match[0];
        else
            return FALSE;
    }
}
